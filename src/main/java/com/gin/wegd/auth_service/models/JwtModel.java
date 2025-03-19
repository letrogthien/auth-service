package com.gin.wegd.auth_service.models;

import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.comon.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_id", length = 36)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TokenStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;
}