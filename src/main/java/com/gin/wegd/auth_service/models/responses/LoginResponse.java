package com.gin.wegd.auth_service.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String status;
    private String tmpToken;
}
