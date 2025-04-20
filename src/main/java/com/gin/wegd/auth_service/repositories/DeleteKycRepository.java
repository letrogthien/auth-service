package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeleteKycRepository extends JpaRepository<DeleteKycRequest, UUID> {
    Optional<DeleteKycRequest> findByUserId(UUID userId);

    List<DeleteKycRequest> findAllByStatus(DKycRequestStatus status);
}