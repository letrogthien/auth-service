package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.DeleteKycRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteKycRepository extends JpaRepository<DeleteKycRequest, Long> {
}