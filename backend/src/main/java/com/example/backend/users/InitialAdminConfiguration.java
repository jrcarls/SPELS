package com.example.backend.users;

import com.example.backend.organizations.Organization;
import com.example.backend.organizations.OrganizationMember;
import com.example.backend.organizations.OrganizationMemberRepository;
import com.example.backend.organizations.OrganizationRepository;
import com.example.backend.organizations.TenantRole;
import com.example.backend.subscriptions.Subscription;
import com.example.backend.subscriptions.SubscriptionPlan;
import com.example.backend.subscriptions.SubscriptionRepository;
import com.example.backend.subscriptions.SubscriptionStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialAdminConfiguration {

    @Bean
    ApplicationRunner initialAdmin(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            OrganizationMemberRepository organizationMemberRepository,
            SubscriptionRepository subscriptionRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.email:}") String email,
            @Value("${app.bootstrap-admin.password:}") String password,
            @Value("${app.bootstrap-admin.name:Administrator}") String name) {
        return arguments -> {
            if (email.isBlank() && password.isBlank()) {
                return;
            }
            if (email.isBlank() || password.isBlank()) {
                throw new IllegalStateException("APP_BOOTSTRAP_ADMIN_EMAIL e APP_BOOTSTRAP_ADMIN_PASSWORD devem ser definidos juntos");
            }

            String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
            User admin = userRepository.findByEmail(normalizedEmail).orElseGet(
                    () -> userRepository.save(new User(name.trim(), normalizedEmail, passwordEncoder.encode(password))));
            admin.setPlatformRole(PlatformRole.ADMIN);
            admin.setActive(true);
            admin = userRepository.save(admin);

            if (organizationMemberRepository.findFirstByUserIdAndActiveTrue(admin.getId()).isEmpty()) {
                Organization organization = organizationRepository.save(
                        new Organization("Platform Administration", "platform-administration", null));
                organizationMemberRepository.save(new OrganizationMember(organization, admin, TenantRole.OWNER));
                subscriptionRepository.save(new Subscription(organization, SubscriptionPlan.PRO, SubscriptionStatus.ACTIVE,
                        Instant.now().plus(100, ChronoUnit.YEARS)));
            }
        };
    }
}
