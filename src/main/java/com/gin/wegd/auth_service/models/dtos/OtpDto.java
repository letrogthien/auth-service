package com.gin.wegd.auth_service.models.dtos;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OtpDto {
    private UUID userId;
    private LocalDateTime createAt;
    private LocalDateTime expiredAt;
    private String otp;
    private OtpPurpose otpPurpose;
    private boolean active;
}
