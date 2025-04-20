package com.gin.wegd.auth_service.models.dtos;

import com.gin.wegd.auth_service.models.Roles;
import com.gin.wegd.auth_service.models.user_attribute.GenderType;
import com.gin.wegd.auth_service.models.user_attribute.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class UserDto {
    private UUID id;
    private String userName;
    private String email;
    private GenderType gender;
    private LocalDate dateOfBirth;
    private List<Roles> role;
    private UserStatus status;
    private String firstName;
    private String lastName;
    private boolean twoFactorAuthEnabled;
}
