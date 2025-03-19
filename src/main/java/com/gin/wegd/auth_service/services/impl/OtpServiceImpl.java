package com.gin.wegd.auth_service.services.impl;


import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.OtpModel;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.repositories.OtpRepository;
import com.gin.wegd.auth_service.services.OtpService;
import com.gin.wegd.auth_service.services.UserService;
import com.gin.wegd.common.events.OtpEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final ProducerService producerService;
    private final OtpRepository otpRepository;
    private final Random random = new Random();
    private final UserService userService;


    @Override
    public OtpModel getOtpValid(String userId,String otp, OtpPurpose otpPurpose) {
        return otpRepository.findValidOtp(userId,otp, otpPurpose, LocalDateTime.now())
                .orElseThrow(()-> new CustomException(ErrorCode.OTP_WRONG));
    }

    @Override
    public void saveOtp(OtpModel otpModel) {
        otpRepository.save(otpModel);
    }

    @Override
    public void sendOtp(OtpModel otpModel) {
        producerService.sendOtpEv(OtpEvModel.newBuilder()
                .setEmail(otpModel.getEmail())
                .setOtp(otpModel.getOtp())
                .setUserName(otpModel.getUserName())
                .build());
    }


    @Override
    public OtpModel generateOtp(User user, OtpPurpose type) {
        int genOtp = this.random.nextInt(9000)+1000;
        OtpModel otpModel = OtpModel.builder()
                .otp(String.valueOf(genOtp))
                .active(true)
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .otpPurpose(type)
                .userId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .build();
        this.saveOtp(otpModel);
        return otpModel;
    }


    @Override
    public void changeToInActive(OtpModel otpModel) {
        otpModel.setActive(false);
        this.saveOtp(otpModel);
    }

    @Override
    public ApiResponse<String> createAndSendOtp(OtpPurpose type) {
        User user = userService.getUserById(this.extractUserIdInContext());
        OtpModel otpModel = this.generateOtp(user, type);
        this.sendOtp(otpModel);
        return ApiResponse.<String>builder()
                .message("otp sent")
                .build();

    }

    private String extractUserIdInContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("id");
    }


}
