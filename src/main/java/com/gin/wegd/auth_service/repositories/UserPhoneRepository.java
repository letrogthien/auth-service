package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.user_attribute.UserPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPhoneRepository extends JpaRepository<UserPhone, UUID> {
    Optional<UserPhone> findByUserId(UUID userId);
}
