package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.models.responses.ApiResponse;

import java.util.UUID;

public interface AdminService {

    ApiResponse<String> banUserById(UUID userId);
    ApiResponse<String> unbanUserById(UUID userId);
    ApiResponse<String> approveKyc(UUID userId);
    ApiResponse<String> rejectKyc(UUID userId);
    ApiResponse<String> approveDeleteKyc(UUID userId);
    ApiResponse<String> rejectDeleteKyc(UUID userId);


}
