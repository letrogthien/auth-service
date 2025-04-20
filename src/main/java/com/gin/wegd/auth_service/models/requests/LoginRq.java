package com.gin.wegd.auth_service.models.requests;

import com.gin.wegd.auth_service.models.StrangeDevice;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRq {
    private String accountName;
    private String password;
    private StrangeDevice deviceInfo;
}
