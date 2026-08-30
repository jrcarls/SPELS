package com.example.backend.auth;

import com.example.backend.organizations.Organization;
import com.example.backend.organizations.OrganizationMember;
import com.example.backend.organizations.OrganizationMemberRepository;
import com.example.backend.organizations.OrganizationRepository;
import com.example.backend.organizations.TenantRole;
import com.example.backend.subscriptions.Subscription;
import com.example.backend.subscriptions.SubscriptionPlan;
import com.example.backend.subscriptions.SubscriptionRepository;
import com.example.backend.subscriptions.SubscriptionStatus;
import com.example.backend.users.User;
import com.example.backend.users.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.text.Normalizer;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long trialDays;

    public AuthService(UserRepository userRepository, OrganizationRepository organizationRepository,
                       OrganizationMemberRepository organizationMemberRepository,
                       SubscriptionRepository subscriptionRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, @Value("${app.subscription.trial-days:14}") long trialDays) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.organizationMemberRepository = organizationMemberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.trialDays = trialDays;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizedEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        String cnpj = normalizedCnpj(request.cnpj());
        if (cnpj != null && organizationRepository.existsByCnpj(cnpj)) {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }
        User user = new User(request.name().trim(), email, passwordEncoder.encode(request.password()));
        userRepository.save(user);
        String organizationName = request.organizationName().trim();
        Organization organization = organizationRepository.save(
                new Organization(organizationName, uniqueSlug(organizationName), cnpj));
        OrganizationMember membership = organizationMemberRepository.save(
                new OrganizationMember(organization, user, TenantRole.OWNER));
        subscriptionRepository.save(new Subscription(organization, SubscriptionPlan.TRIAL, SubscriptionStatus.TRIAL,
                Instant.now().plus(trialDays, ChronoUnit.DAYS)));
        return response(user, membership);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizedEmail(request.email()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }
        if (!user.isActive()) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }
        OrganizationMember membership = organizationMemberRepository.findFirstByUserIdAndActiveTrue(user.getId())
                .filter(member -> member.getOrganization().isActive())
                .orElseThrow(() -> new IllegalArgumentException("Conta sem organização ativa"));
        return response(user, membership);
    }

    private AuthResponse response(User user, OrganizationMember membership) {
        return new AuthResponse(jwtService.generateToken(user, membership), membership.getOrganization().getPublicId());
    }

    private String normalizedEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizedCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            return null;
        }
        String value = cnpj.trim().toUpperCase(Locale.ROOT);
        if (!value.matches("[A-Z0-9./-]+")) {
            throw new IllegalArgumentException("CNPJ contém caracteres inválidos");
        }
        String canonical = value.replaceAll("[./-]", "");
        if (!canonical.matches("[A-Z0-9]{12}\\d{2}")) {
            throw new IllegalArgumentException("CNPJ deve ter 12 caracteres alfanuméricos e 2 dígitos verificadores");
        }
        return canonical;
    }

    private String uniqueSlug(String organizationName) {
        String slug = Normalizer.normalize(organizationName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.isBlank()) {
            slug = "confeitaria";
        }

        String candidate = slug;
        int suffix = 2;
        while (organizationRepository.existsBySlug(candidate)) {
            candidate = slug + "-" + suffix++;
        }
        return candidate;
    }
}
