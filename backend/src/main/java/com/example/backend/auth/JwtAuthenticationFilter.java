package com.example.backend.auth;

import com.example.backend.users.UserRepository;
import com.example.backend.organizations.OrganizationMember;
import com.example.backend.organizations.OrganizationMemberRepository;
import com.example.backend.subscriptions.SubscriptionService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final SubscriptionService subscriptionService;
    private final TenantContext tenantContext;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository,
                                   OrganizationMemberRepository organizationMemberRepository,
                                   SubscriptionService subscriptionService, TenantContext tenantContext) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.organizationMemberRepository = organizationMemberRepository;
        this.subscriptionService = subscriptionService;
        this.tenantContext = tenantContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                String token = header.substring(7);
                if (jwtService.isValid(token)) {
                    Claims claims = jwtService.extractClaims(token);
                    Long organizationId = claims.get("organizationId", Number.class).longValue();
                    userRepository.findByEmail(claims.getSubject())
                            .filter(user -> user.isActive())
                            .flatMap(user -> organizationMemberRepository
                                    .findFirstByUserIdAndOrganizationIdAndActiveTrue(user.getId(), organizationId)
                                    .filter(member -> member.getOrganization().isActive()))
                            .filter(member -> subscriptionService.hasAccess(organizationId))
                            .ifPresent(member -> authenticate(member, organizationId));
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            tenantContext.clear();
        }
    }

    private void authenticate(OrganizationMember member, Long organizationId) {
        var authentication = new UsernamePasswordAuthenticationToken(member.getUser().getEmail(), null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_" + member.getRole().name()),
                        new SimpleGrantedAuthority("ROLE_" + member.getUser().getPlatformRole().name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        tenantContext.setOrganizationId(organizationId);
    }
}
