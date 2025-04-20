package com.gin.wegd.auth_service.services;



import com.gin.wegd.auth_service.models.requests.*;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import org.springframework.web.multipart.MultipartFile;



public interface UserCenterService {
    ApiResponse<String> forgetPassword(final ForgetPasswordRq forgetPasswordRq);
    ApiResponse<String> changePassword(final ChangePasswordRq changPasswordRq);

    ApiResponse<String> deleteAccount(String otp);

    ApiResponse<String> verifyPhoneNumber(String otp);
    ApiResponse<String> changePhoneNumber(ChangePhoneNumberRq changePhoneNumberRq);

    ApiResponse<String> updateProfile(UpdateUserRq updateUserRq);

    ApiResponse<String> enable2fa();
    ApiResponse<String> disable2fa(String otp);

    ApiResponse<String> registerMiddleMan();
    ApiResponse<String> addKyc(MultipartFile frontId, MultipartFile backId);
    ApiResponse<String> deleteKyc(String otp, MultipartFile verifyImg);
    ApiResponse<String> updateAddress(UpdateAddressRq addressRq);
    ApiResponse<String> updateInstanceMessage(ChangeInstanceMessageRq instanceMessageRq);
}
