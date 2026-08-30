package com.example.backend.subscriptions;

import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public boolean hasAccess(Long organizationId) {
        return subscriptionRepository.findByOrganizationId(organizationId)
                .map(subscription -> (subscription.getStatus() == SubscriptionStatus.TRIAL
                        || subscription.getStatus() == SubscriptionStatus.ACTIVE)
                        && subscription.getCurrentPeriodEndsAt().isAfter(Instant.now()))
                .orElse(false);
    }
}
