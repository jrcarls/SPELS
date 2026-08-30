package com.example.backend.organizations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(unique = true, length = 14)
    private String cnpj;

    @Column(nullable = false)
    private boolean active = true;

    protected Organization() {
    }

    public Organization(String name, String slug, String cnpj) {
        this.name = name;
        this.slug = slug;
        this.cnpj = cnpj;
    }

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getCnpj() { return cnpj; }
    public boolean isActive() { return active; }
    public void setName(String name) { this.name = name; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public void setActive(boolean active) { this.active = active; }

    @PrePersist
    private void ensurePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }
}
