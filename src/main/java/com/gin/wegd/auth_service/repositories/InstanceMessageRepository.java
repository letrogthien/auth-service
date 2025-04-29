package com.gin.wegd.auth_service.repositories;


import com.gin.wegd.auth_service.models.user_attribute.InstanceMessageClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface InstanceMessageRepository extends JpaRepository<InstanceMessageClass, UUID> {
    Optional<InstanceMessageClass> findByUserId(UUID userId);
}
