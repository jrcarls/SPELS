package com.example.backend.organizations;

import com.example.backend.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "organization_members", uniqueConstraints =
        @UniqueConstraint(name = "uk_organization_member", columnNames = {"organization_id", "user_id"}))
public class OrganizationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TenantRole role;

    @Column(nullable = false)
    private boolean active = true;

    protected OrganizationMember() {
    }

    public OrganizationMember(Organization organization, User user, TenantRole role) {
        this.organization = organization;
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }
    public Organization getOrganization() { return organization; }
    public User getUser() { return user; }
    public TenantRole getRole() { return role; }
    public boolean isActive() { return active; }
    public void setRole(TenantRole role) { this.role = role; }
    public void setActive(boolean active) { this.active = active; }
}
