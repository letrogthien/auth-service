package com.gin.wegd.auth_service.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String message;
}
