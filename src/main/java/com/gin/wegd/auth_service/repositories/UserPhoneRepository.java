package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.user_attribute.UserPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, UUID> {
    Optional<UserPhone> findByUserId(UUID userId);
}
