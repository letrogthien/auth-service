package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.models.UserIdDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserIdDocumentRepository extends JpaRepository<UserIdDocument, UUID> {
    UserIdDocument findByUserId(UUID userId);
}
