package com.gin.wegd.auth_service.repositories;


import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Roles, UUID> {
    Optional<Roles> findByName(Role name);
}
