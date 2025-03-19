package com.gin.wegd.auth_service.models.requests;



import com.gin.wegd.auth_service.comon.*;
import com.gin.wegd.auth_service.models.user_attribute.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUserRq {
    private GenderType gender;
    private LocalDate dateOfBirth;
    private InstanceMessageClass instanceMessage;
    private List<Role> role;
    private UserStatus status;
    private Address address;
    private UserPhone phone;
    private String firstName;
    private String lastName;
}
