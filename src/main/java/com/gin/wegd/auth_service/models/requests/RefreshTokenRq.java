package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenRq {
    private String refreshToken;
}
