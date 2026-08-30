package com.example.backend.auth;

import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    private final ThreadLocal<Long> organizationId = new ThreadLocal<>();

    public void setOrganizationId(Long id) {
        organizationId.set(id);
    }

    public Long getOrganizationId() {
        return organizationId.get();
    }

    public void clear() {
        organizationId.remove();
    }
}
