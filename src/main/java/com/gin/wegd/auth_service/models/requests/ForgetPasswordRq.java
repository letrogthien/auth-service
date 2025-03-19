package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgetPasswordRq {
    private String email;
}
