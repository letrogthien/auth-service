package com.gin.wegd.auth_service.repositories;


import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.comon.TokenType;
import com.gin.wegd.auth_service.models.JwtModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JwtModelRepository extends JpaRepository<JwtModel, UUID> {

    List<JwtModel> findByUserIdAndTokenTypeAndStatus(UUID userId, TokenType tokenType, TokenStatus status);
    Optional<JwtModel> findByToken(String token);
}
