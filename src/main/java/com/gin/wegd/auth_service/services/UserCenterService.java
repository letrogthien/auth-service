package com.gin.wegd.auth_service.services;



import com.gin.wegd.auth_service.models.requests.ChangePasswordRq;
import com.gin.wegd.auth_service.models.requests.ChangePhoneNumberRq;
import com.gin.wegd.auth_service.models.requests.ForgetPasswordRq;
import com.gin.wegd.auth_service.models.requests.UpdateUserRq;
import com.gin.wegd.auth_service.models.responses.ApiResponse;


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
}
