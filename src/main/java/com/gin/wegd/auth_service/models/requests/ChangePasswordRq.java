package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordRq {
    private String otp;
    private String newPassword;
    private String oldPassword;
}
