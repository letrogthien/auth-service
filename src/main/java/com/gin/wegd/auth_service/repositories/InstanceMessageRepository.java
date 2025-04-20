package com.gin.wegd.auth_service.repositories;


import com.gin.wegd.auth_service.models.user_attribute.InstanceMessageClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstanceMessageRepository extends JpaRepository<InstanceMessageClass, UUID> {
    Optional<InstanceMessageClass> findByUserId(UUID userId);
    // Custom query methods can be defined here if needed
}
