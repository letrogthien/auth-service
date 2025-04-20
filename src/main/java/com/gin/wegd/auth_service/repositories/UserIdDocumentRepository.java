package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.models.UserIdDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserIdDocumentRepository extends JpaRepository<UserIdDocument, UUID> {
    Optional<UserIdDocument> findByUserId(UUID userId);

    List<UserIdDocument> findAllByStatus(UserIdDocStatus userIdDocStatus);
}
