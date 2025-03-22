package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.requests.*;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.models.responses.LoginResponse;
import com.gin.wegd.auth_service.models.responses.RegisterResponse;
import com.gin.wegd.auth_service.services.AuthService;
import com.gin.wegd.common.events.OtpEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ProducerService producerService;

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login (@RequestBody final LoginRq loginRq) {
        return authService.login(loginRq);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register (@RequestBody final RegisterRq registerRq) {
        return authService.register(registerRq);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh-token")
    public ApiResponse<LoginResponse> refreshToken (@RequestParam final String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ApiResponse<String> logout (@RequestHeader("Authorization") final String refreshToken) {
        return authService.logout(refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout-all")
    public ApiResponse<String> logoutAll (@RequestHeader("Authorization") final String refreshToken) {
        return authService.logoutAll(refreshToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify-2fa")
    public ApiResponse<LoginResponse> verify2Fa (@RequestBody final Verify2FaRq verify2FaRq) {
        return authService.verify2Fa(verify2FaRq);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/test")
    public String test(){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "success";
    }



}
