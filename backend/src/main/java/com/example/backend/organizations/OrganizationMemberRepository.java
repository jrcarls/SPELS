package com.example.backend.organizations;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    @EntityGraph(attributePaths = {"organization", "user"})
    Optional<OrganizationMember> findFirstByUserIdAndActiveTrue(Long userId);

    @EntityGraph(attributePaths = {"organization", "user"})
    Optional<OrganizationMember> findFirstByUserIdAndOrganizationIdAndActiveTrue(
            Long userId, Long organizationId);
}
