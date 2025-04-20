package com.gin.wegd.auth_service.models.requests;

import com.gin.wegd.auth_service.comon.Role;
import lombok.Data;

import java.util.UUID;


@Data
public class AuthorizeUserRq {
    private Role role;
    private UUID userId;
}
