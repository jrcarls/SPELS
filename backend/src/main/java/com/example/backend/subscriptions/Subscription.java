package com.example.backend.subscriptions;

import com.example.backend.organizations.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Instant currentPeriodEndsAt;

    @Column(length = 120)
    private String providerCustomerId;

    protected Subscription() {
    }

    public Subscription(Organization organization, SubscriptionPlan plan, SubscriptionStatus status,
                        Instant currentPeriodEndsAt) {
        this.organization = organization;
        this.plan = plan;
        this.status = status;
        this.currentPeriodEndsAt = currentPeriodEndsAt;
    }

    public Long getId() { return id; }
    public Organization getOrganization() { return organization; }
    public SubscriptionPlan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public Instant getCurrentPeriodEndsAt() { return currentPeriodEndsAt; }
    public String getProviderCustomerId() { return providerCustomerId; }
    public void setPlan(SubscriptionPlan plan) { this.plan = plan; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public void setCurrentPeriodEndsAt(Instant currentPeriodEndsAt) { this.currentPeriodEndsAt = currentPeriodEndsAt; }
    public void setProviderCustomerId(String providerCustomerId) { this.providerCustomerId = providerCustomerId; }
}
