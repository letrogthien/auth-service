package com.gin.wegd.auth_service.controllers;


import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.models.requests.AuthorizeUserRq;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/ban")
    public ApiResponse<String> banUser(@RequestParam UUID userId) {
        return adminService.banUserById(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/unban")
    public ApiResponse<String> unbanUser(@RequestParam UUID userId) {
        return adminService.unbanUserById(userId);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/kyc/approve")
    public ApiResponse<String> approveKyc(@RequestParam UUID kycId) {
        return adminService.approveKyc(kycId);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/kyc/reject")
    public ApiResponse<String> rejectKyc(@RequestParam UUID kycId) {
        return adminService.rejectKyc(kycId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/kyc/get/all/status")
    public ApiResponse<List<UserIdDocument>> getUserIdDocumentPending() {
        return adminService.getAllUserIdDocumentsPending();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/kyc/get/{id}")
    public ApiResponse<UserIdDocument> getDocumentIdById(@PathVariable UUID id) {
        return adminService.getUserIdDocumentByUserId(id);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("kyc/approve-delete")
    public ApiResponse<String> approveDeleteKyc(@RequestParam UUID userId) {
        return adminService.approveDeleteKyc(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/kyc/reject-delete")
    public ApiResponse<String> rejectDeleteKyc(@RequestParam UUID userId) {
        return adminService.rejectDeleteKyc(userId);
    }

    @GetMapping("/kyc/get/delete-request/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequests() {
        return adminService.getAllDeleteKycRequests();
    }

    @GetMapping("/kyc/get/delete-request/all/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequestsByStatus(@RequestParam DKycRequestStatus status) {
        return adminService.getAllDeleteKycRequestsByStatus(status);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/role/add")
    public ApiResponse<String> addAuthorizeUser(@RequestBody AuthorizeUserRq authorizeUserRq) {
        return adminService.addAuthorizeUser(authorizeUserRq.getUserId(), authorizeUserRq.getRole());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/role/remove")
    public ApiResponse<String> removeAuthorizeUser(@RequestBody AuthorizeUserRq authorizeUserRq) {
        return adminService.removeAuthorizeUser(authorizeUserRq.getUserId(), authorizeUserRq.getRole());
    }



}
