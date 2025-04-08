package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/ban")
    public ApiResponse<String> banUser(@RequestParam UUID userId) {
        return adminService.banUserById(userId);
    }
    @PostMapping("/unban")
    public ApiResponse<String> unbanUser(@RequestParam UUID userId) {
        return adminService.unbanUserById(userId);
    }

    @PostMapping("/approve-kyc")
    public ApiResponse<String> approveKyc(@RequestParam UUID userId) {
        return adminService.approveKyc(userId);
    }

    @PostMapping("/reject-kyc")
    public ApiResponse<String> rejectKyc(@RequestParam UUID userId) {
        return adminService.rejectKyc(userId);
    }

    @PostMapping("/approve-delete-kyc")
    public ApiResponse<String> approveDeleteKyc(@RequestParam UUID userId) {
        return adminService.approveDeleteKyc(userId);
    }
    @PostMapping("/reject-delete-kyc")
    public ApiResponse<String> rejectDeleteKyc(@RequestParam UUID userId) {
        return adminService.rejectDeleteKyc(userId);
    }

}
