package com.example.backend.organizations;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsBySlug(String slug);
    boolean existsByCnpj(String cnpj);
    Optional<Organization> findBySlug(String slug);
}
