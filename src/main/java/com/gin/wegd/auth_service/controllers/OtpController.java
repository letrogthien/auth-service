package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/otp")
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/generate")
    public void generateOtp(@RequestBody OtpPurpose purpose){
        otpService.createAndSendOtp(purpose);

    }
}
