package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.models.StrangeDevice;
import com.gin.wegd.auth_service.models.requests.*;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.models.responses.LoginResponse;
import com.gin.wegd.auth_service.models.responses.RegisterResponse;


public interface AuthService {
    ApiResponse<LoginResponse> login(final LoginRq loginRequest);
    ApiResponse<RegisterResponse> register(final RegisterRq registerRequest);
    ApiResponse<LoginResponse> refreshToken(String refreshToken);
    ApiResponse<String> logout(String refreshToken);
    ApiResponse<String>logoutAll(String refreshToken);
    ApiResponse<LoginResponse> verify2Fa(Verify2FaRq rq);
    ApiResponse<String> trustDevice(StrangeDevice strangeDevice);
    void test();
}