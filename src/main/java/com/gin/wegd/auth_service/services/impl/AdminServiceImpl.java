package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.AdminService;
import com.gin.wegd.auth_service.services.UserService;
import com.gin.wegd.common.events.BaseNotifyEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final ProducerService producerService;


    private void sendBaseMail(String data, UUID userId) {
        BaseNotifyEmail baseNotifyEmail = BaseNotifyEmail.newBuilder()
                .setEmail(userService.getUserById(userId).getEmail())
                .setData(data)
                .build();
        producerService.baseNotifyEv(baseNotifyEmail);
    }
    @Override
    public ApiResponse<String> banUserById(UUID userId) {
        userService.banUser(userId);
        sendBaseMail("Your account has been banned", userId);
        return ApiResponse.<String>builder()
                .status(200)
                .message("User banned successfully")
                .data("User with ID: " + userId + " has been banned.")
                .build();
    }

    @Override
    public ApiResponse<String> unbanUserById(UUID userId) {
        ///  sent email to user
        userService.unbanUser(userId);
        sendBaseMail("Your account has been unbanned", userId);
        return ApiResponse.<String>builder()
                .status(200)
                .message("User unbanned successfully")
                .data("User with ID: " + userId + " has been unbanned.")
                .build();
    }

    @Override
    public ApiResponse<String> approveKyc(UUID userId) {
        ///  sent email to user
        User user= userService.getUserById(userId);
        sendBaseMail("Your KYC has been approved", userId);
        user.getUserIdDocument().setStatus(UserIdDocStatus.VERIFIED);
        userService.saveUser(user);
        return ApiResponse.<String>builder()
                .status(200)
                .message("KYC approved successfully")
                .data("User with ID: " + userId + " has been approved.")
                .build();
    }

    @Override
    public ApiResponse<String> rejectKyc(UUID userId) {
        ///  sent email to user
        User user= userService.getUserById(userId);
        sendBaseMail("Your KYC has been rejected", userId);
        user.getUserIdDocument().setStatus(UserIdDocStatus.REJECTED);
        userService.saveUser(user);
        return ApiResponse.<String>builder()
                .status(200)
                .message("KYC rejected successfully")
                .data("User with ID: " + userId + " has been rejected.")
                .build();
    }

    @Override
    public ApiResponse<String> approveDeleteKyc(UUID userId) {
        User user= userService.getUserById(userId);
        user.setUserIdDocument(null);
        userService.saveUser(user);
        sendBaseMail("Your KYC deletion has been approved", userId);
        return ApiResponse.<String>builder()
                .status(200)
                .message("Delete KYC approved successfully")
                .data("User with ID: " + userId + " has been approved.")
                .build();
    }

    @Override
    public ApiResponse<String> rejectDeleteKyc(UUID userId) {
        ///  sent email to user
        sendBaseMail("Your KYC deletion has been rejected", userId);
        return ApiResponse.<String>builder()
                .status(200)
                .message("Delete KYC rejected successfully")
                .data("User with ID: " + userId + " has been rejected.")
                .build();
    }
}
