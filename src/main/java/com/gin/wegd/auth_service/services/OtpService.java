package com.gin.wegd.auth_service.services;


import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.models.OtpModel;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.responses.ApiResponse;

import java.util.UUID;


public interface OtpService {
    OtpModel getOtpValid(UUID userId, String otp, OtpPurpose type);
    void saveOtp(OtpModel otpModel);
    void sendOtp(OtpModel otpModel);
    OtpModel generateOtp(User user, OtpPurpose type);
    void changeToInActive(OtpModel otpModel);

    ApiResponse<String> createAndSendOtp(OtpPurpose type);

}