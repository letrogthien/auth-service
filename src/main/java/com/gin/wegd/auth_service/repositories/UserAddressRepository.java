package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.user_attribute.Address;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findByUserId(UUID userId);
}
