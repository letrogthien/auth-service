package com.gin.wegd.auth_service.controllers;

import com.gin.wegd.auth_service.models.requests.ChangePasswordRq;
import com.gin.wegd.auth_service.models.requests.ChangePhoneNumberRq;
import com.gin.wegd.auth_service.models.requests.ForgetPasswordRq;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/userCenter")
public class UserCenterController {
    private final UserCenterService userCenterService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forget-password")
    public ApiResponse<String> forgetPassword (@RequestBody final ForgetPasswordRq forgetPasswordRq) {
        return userCenterService.forgetPassword(forgetPasswordRq);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/change-password")
    public ApiResponse<String> changePasswordRequest (@RequestBody final ChangePasswordRq changPasswordRq) {
        return userCenterService.changePassword(changPasswordRq);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public ApiResponse<String> deleteAccount(@RequestBody final String otp) {
        return userCenterService.deleteAccount(otp);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify-phone-number")
    public ApiResponse<String> verifyPhoneNumber(@RequestBody final String otp) {
        return userCenterService.verifyPhoneNumber(otp);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/change-phone-number")
    public ApiResponse<String> changePhoneNumber(@RequestBody final ChangePhoneNumberRq changePhoneNumberRq) {
        return userCenterService.changePhoneNumber(changePhoneNumberRq);
    }

}
