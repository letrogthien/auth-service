package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRq {
    private String accountName;
    private String password;
    private String deviceInfo;
}
