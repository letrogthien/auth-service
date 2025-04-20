package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.models.Roles;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.repositories.RoleRepository;
import com.gin.wegd.auth_service.services.AdminService;
import com.gin.wegd.auth_service.services.DeleteKycService;
import com.gin.wegd.auth_service.services.UserIdDocumentService;
import com.gin.wegd.auth_service.services.UserService;
import com.gin.wegd.common.events.BaseNotifyEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final ProducerService producerService;
    private final DeleteKycService deleteKycService;
    private final UserIdDocumentService userIdDocumentService;
    private final RoleRepository    roleRepository;


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
                .message("User banned successfully")
                .data(userId + " has been banned.")
                .build();
    }

    @Override
    public ApiResponse<String> unbanUserById(UUID userId) {
        userService.unbanUser(userId);
        sendBaseMail("Your account has been unbanned", userId);
        return ApiResponse.<String>builder()
                .message("User unbanned successfully")
                .data( userId + " has been unbanned.")
                .build();
    }

    @Override
    public ApiResponse<String> approveKyc(UUID kycId) {
        userIdDocumentService.verifyDocument(kycId);
        sendBaseMail("Your KYC has been approved", kycId);
        return ApiResponse.<String>builder()
                .message("KYC approved successfully")
                .data("kyc has been approved.")
                .build();
    }

    @Override
    public ApiResponse<String> rejectKyc(UUID kycId) {
        userIdDocumentService.rejectDocument(kycId);
        sendBaseMail("Your KYC has been rejected", kycId);

        return ApiResponse.<String>builder()
                .message("KYC rejected successfully")
                .data(" has been rejected.")
                .build();
    }

    @Override
    public ApiResponse<String> approveDeleteKyc(UUID userId) {
        User user= userService.getUserById(userId);
        user.setUserIdDocument(null);
        deleteKycService.approve(userId);
        userService.saveUser(user);
        sendBaseMail("Your KYC deletion has been approved", userId);
        return ApiResponse.<String>builder()
                .message("Delete KYC approved successfully")
                .data("User with ID: " + userId + " has been approved.")
                .build();
    }

    @Override
    public ApiResponse<String> rejectDeleteKyc(UUID userId) {
        sendBaseMail("Your KYC deletion has been rejected", userId);
        return ApiResponse.<String>builder()
                .message("Delete KYC rejected successfully")
                .data("User with ID: " + userId + " has been rejected.")
                .build();
    }

    @Override
    public ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequests() {
        return ApiResponse.<List<DeleteKycRequest>>builder()
                .message("All delete KYC requests")
                .data(deleteKycService.getAllDeleteKycRequests())
                .build();
    }

    @Override
    public ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequestsByStatus(DKycRequestStatus status) {
        return ApiResponse.<List<DeleteKycRequest>>builder()
                .message("All delete KYC requests by status")
                .data(deleteKycService.getAllDeleteKycRequestsByStatus(status))
                .build();
    }

    @Override
    public ApiResponse<UserIdDocument> getUserIdDocumentByUserId(UUID kycId) {
        UserIdDocument userIdDocument = userIdDocumentService.getDocument(kycId);
        return ApiResponse.<UserIdDocument>builder()
                .message("User ID Document")
                .data(userIdDocument)
                .build();
    }

    @Override
    public ApiResponse<List<UserIdDocument>> getAllUserIdDocumentsPending() {
        List<UserIdDocument> userIdDocuments =
                userIdDocumentService.getAllUserIdDocumentsByStatus(UserIdDocStatus.PENDING);
        return ApiResponse.<List<UserIdDocument>>builder()
                .message("All user ID documents pending")
                .data(userIdDocuments)
                .build();
    }

    @Override
    public ApiResponse<String> addAuthorizeUser(UUID userId, Role role) {
        User user = userService.getUserById(userId);
        if (user.getRole().stream().map(Roles::getName).toList().contains(role)) {
            return ApiResponse.<String>builder()
                    .message("User already has this role")
                    .build();
        }
        Roles r = roleRepository.findByName(role)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        user.getRole().add(r);
        userService.saveUser(user);
        return ApiResponse.<String>builder()
                .message("User role added successfully")
                .data(userId + " has been added to " + role)
                .build();
    }

    @Override
    public ApiResponse<String> removeAuthorizeUser(UUID userId, Role role) {
        User user = userService.getUserById(userId);
        Roles roles = user.getRole().stream()
                .filter(r -> r.getName() == role)
                .findFirst()
                .orElse(null);
        if (roles == null) {
            return ApiResponse.<String>builder()
                    .message("User does not have the role: " + role)
                    .build();
        }

        user.removeRole(role);
        userService.saveUser(user);
        return ApiResponse.<String>builder()
                .message("User role removed successfully")
                .data(userId + " has been removed from " + role)
                .build();
    }


}
