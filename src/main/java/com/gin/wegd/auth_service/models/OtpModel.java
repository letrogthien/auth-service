package com.gin.wegd.auth_service.models;
import com.gin.wegd.auth_service.comon.OtpPurpose;
import lombok.*;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OtpModel {


    private UUID userId;

    private String userName;

    private String email;


    private String otp;


    private OtpPurpose otpPurpose;



}