package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.comon.PasswordGenerator;
import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import com.gin.wegd.auth_service.config.CustomPasswordEncoder;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.mapper.UserMapper;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.models.OtpModel;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.models.requests.ChangePasswordRq;
import com.gin.wegd.auth_service.models.requests.ChangePhoneNumberRq;
import com.gin.wegd.auth_service.models.requests.ForgetPasswordRq;
import com.gin.wegd.auth_service.models.requests.UpdateUserRq;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.models.user_attribute.UserPhone;
import com.gin.wegd.auth_service.models.user_attribute.UserStatus;
import com.gin.wegd.auth_service.services.*;
import com.gin.wegd.common.events.ForgotPasswordEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserCenterServiceImpl implements UserCenterService {
    private final UserService userService;
    private final CustomPasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final ProducerService producerService;
    private final PasswordGenerator passwordGenerator;
    private final UserMapper userMapper;
    private final UserIdDocumentService userIdDocumentService;
    private final UtilsService utilsService;
    private final DeleteKycService deleteKycService;


    @Override
    public ApiResponse<String> forgetPassword(ForgetPasswordRq forgetPasswordRq) {
        User u = userService.getUserByEmail(forgetPasswordRq.getEmail());
        String pass = passwordGenerator.generatePassword(12);
        u.setPassword(passwordEncoder.passwordEncoder().encode(pass));
        userService.saveUser(u);
        producerService.forgotPasswordEv(ForgotPasswordEvModel.newBuilder()
                .setEmail(u.getEmail())
                .setNewPassword(pass)
                .setUserName(u.getUserName())
                .build());
        return ApiResponse.<String>builder()
                .message("Send new password success")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<String> changePassword(ChangePasswordRq changePasswordRq) {
        UUID userId = this.extractUserIdInContext();

        User u = userService.getUserById(userId);

        if (!checkPassword(changePasswordRq, u)) {
            throw new CustomException(ErrorCode.CURRENT_PASSWORD_INVALID);
        }
        OtpModel otpModel = otpService.getOtpValid(userId, changePasswordRq.getOtp(), OtpPurpose.CHANGE_PASSWORD);
        otpService.deleteOtp(otpModel);

        u.setPassword(passwordEncoder.passwordEncoder().encode(changePasswordRq.getNewPassword()));
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("change password success")
                .build();
    }


    @Transactional
    @Override
    public ApiResponse<String> deleteAccount(String otp) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        if (u.getStatus() == UserStatus.BANNED || u.getStatus() == UserStatus.INACTIVE) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        OtpModel otpModel = otpService.getOtpValid(userId, otp, OtpPurpose.DELETE_ACCOUNT);
        u.setStatus(UserStatus.INACTIVE);
        userService.saveUser(u);
        otpService.deleteOtp(otpModel);
        return ApiResponse.<String>builder()
                .message("delete success")
                .build();
    }


    @Override
    @Transactional
    public ApiResponse<String> verifyPhoneNumber(String otp) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        if (u.getPhone().isActive()) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        OtpModel otpModel = otpService.getOtpValid(userId, otp, OtpPurpose.VERIFY_PHONE_NUMBER);
        otpService.deleteOtp(otpModel);
        u.getPhone().setActive(true);
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("phone number verified")
                .build();
    }


    @Override
    @Transactional
    public ApiResponse<String> changePhoneNumber(ChangePhoneNumberRq changePhoneNumberRq) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);

        UserPhone userPhone = u.getPhone();
        if (userPhone.isActive() && changePhoneNumberRq.getOtp() != null) {
            OtpModel otpModel = otpService.getOtpValid(userId,
                    changePhoneNumberRq.getOtp(),
                    OtpPurpose.CHANGE_PHONE_NUMBER);
            otpService.deleteOtp(otpModel);
        }
        userPhone.setPhone(changePhoneNumberRq.getPhoneNumber());
        userPhone.setActive(true);
        u.setPhone(userPhone);
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("change phone number success")
                .build();
    }

    @Override
    public ApiResponse<String> updateProfile(UpdateUserRq updateUserRq) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        u = userMapper.updateUserRqToUser(updateUserRq, u);
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("update profile success")
                .build();
    }

    @Override
    public ApiResponse<String> enable2fa() {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        u.setTwoFactorAuthEnabled(true);
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("enable 2fa success")
                .build();
    }

    @Override
    public ApiResponse<String> disable2fa(String otp) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        u.setTwoFactorAuthEnabled(false);
        otpService.getOtpValid(userId, otp, OtpPurpose.TWO_FACTOR_AUTHENTICATION);
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("disable 2fa success")
                .build();
    }

    @Override
    public ApiResponse<String> registerMiddleMan() {
        User u = userService.getUserById(this.extractUserIdInContext());
        if (u.getRole().contains(Role.MIDDLE_MAN)) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        UserIdDocument userIdDocument = userIdDocumentService.getDocuments(u.getId());
        if (userIdDocument.getStatus() != UserIdDocStatus.VERIFIED) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        u.getRole().add(Role.MIDDLE_MAN); //wait for admin to approve
        userService.saveUser(u);
        return ApiResponse.<String>builder()
                .message("register middle")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<String> addKyc(MultipartFile frontId, MultipartFile backId){
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        if (u.getUserIdDocument() != null) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        String front = utilsService.saveKycFile(frontId);
        String back = utilsService.saveKycFile(backId);
        UserIdDocument userIdDocument = new UserIdDocument();
        userIdDocument.setUser(u);
        userIdDocument.setFrontId(front);
        userIdDocument.setBackId(back);
        userIdDocument.setStatus(UserIdDocStatus.PENDING);
        userIdDocumentService.createDocument(u, front, back);
        return ApiResponse.<String>builder()
                .message("add kyc success, wait for admin to approve")
                .build();

    }


    ///      it should be implemented in the future
    ///     to add the deleteKyc method with Image verification to verify by admin
    @Override
    public ApiResponse<String> deleteKyc(String otp, MultipartFile verifyImg) {
        UUID userId = this.extractUserIdInContext();
        User u = userService.getUserById(userId);
        if (u.getUserIdDocument() == null) {
            throw new CustomException(ErrorCode.BLOCK_REQUEST);
        }
        OtpModel otpModel = otpService.getOtpValid(userId, otp, OtpPurpose.DELETE_KYC);
        otpService.deleteOtp(otpModel);
        String verifyImgPath = utilsService.saveKycFile(verifyImg);
        DeleteKycRequest deleteKycRequest = DeleteKycRequest.builder()
                .verifyImgPath(verifyImgPath)
                .userId(userId).build();
        deleteKycService.saveObj(deleteKycRequest);
        userIdDocumentService.deleteDocument(u.getUserIdDocument().getId());
        return ApiResponse.<String>builder()
                .message("delete kyc success")
                .build();
    }


    private boolean checkPassword(ChangePasswordRq changePasswordRq, User user) {
        return passwordEncoder.passwordEncoder().matches(changePasswordRq.getOldPassword(), user.getPassword());
    }


    private UUID extractUserIdInContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return UUID.fromString(jwt.getClaim("id"));
    }


}
