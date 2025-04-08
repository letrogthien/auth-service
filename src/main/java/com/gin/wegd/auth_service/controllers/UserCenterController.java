package com.gin.wegd.auth_service.controllers;

import com.gin.wegd.auth_service.models.requests.ChangePasswordRq;
import com.gin.wegd.auth_service.models.requests.ChangePhoneNumberRq;
import com.gin.wegd.auth_service.models.requests.ForgetPasswordRq;
import com.gin.wegd.auth_service.models.requests.UpdateUserRq;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/userCenter")
public class UserCenterController {
    private final UserCenterService userCenterService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/forget-password")
    public ApiResponse<String> forgetPassword(@RequestBody final ForgetPasswordRq forgetPasswordRq) {
        return userCenterService.forgetPassword(forgetPasswordRq);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/change-password")
    public ApiResponse<String> changePasswordRequest(@RequestBody final ChangePasswordRq changPasswordRq) {
        return userCenterService.changePassword(changPasswordRq);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public ApiResponse<String> deleteAccount(@RequestBody final String otp) {
        return userCenterService.deleteAccount(otp);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify-phone-number")
    public ApiResponse<String> verifyPhoneNumber(@RequestBody final String otp) {
        return userCenterService.verifyPhoneNumber(otp);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/change-phone-number")
    public ApiResponse<String> changePhoneNumber(@RequestBody final ChangePhoneNumberRq changePhoneNumberRq) {
        return userCenterService.changePhoneNumber(changePhoneNumberRq);
    }


    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update-profile")
    public ApiResponse<String> updateProfile(@RequestBody final UpdateUserRq updateUserRq) {
        return userCenterService.updateProfile(updateUserRq);
    }


    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/enable-2fa")
    public ApiResponse<String> enable2fa() {
        return userCenterService.enable2fa();
    }


    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/disable-2fa")
    public ApiResponse<String> disable2fa(@RequestBody final String otp) {
        return userCenterService.disable2fa(otp);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register-middleMan")
    public ApiResponse<String> registerMiddleMan() {
        return userCenterService.registerMiddleMan();
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add-kyc")
    public ApiResponse<String> addKyc(@RequestBody final MultipartFile frontId, @RequestBody final MultipartFile backId) {
        return userCenterService.addKyc(frontId, backId);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete-kyc")
    public ApiResponse<String> deleteKyc(@RequestBody final String otp, @RequestBody final MultipartFile verifyImg,
                                         String id) {
        return userCenterService.deleteKyc(otp, verifyImg);
    }

}
