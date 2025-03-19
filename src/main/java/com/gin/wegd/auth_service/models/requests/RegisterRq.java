package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RegisterRq {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
