package com.gin.wegd.auth_service.models;
import com.gin.wegd.auth_service.comon.OtpPurpose;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpModel {
    @Id
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "user_id", length = 36)
    private UUID userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(name = "otp_purpose")
    private OtpPurpose otpPurpose;

    @Column(name = "active", nullable = false)
    private boolean active;
}