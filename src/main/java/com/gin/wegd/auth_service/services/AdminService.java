package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.models.UserIdDocument;
import com.gin.wegd.auth_service.models.responses.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    ApiResponse<String> banUserById(UUID userId);
    ApiResponse<String> unbanUserById(UUID userId);
    ApiResponse<String> approveKyc(UUID userId);
    ApiResponse<String> rejectKyc(UUID userId);
    ApiResponse<String> approveDeleteKyc(UUID userId);
    ApiResponse<String> rejectDeleteKyc(UUID userId);
    ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequests();
    ApiResponse<List<DeleteKycRequest>> getAllDeleteKycRequestsByStatus(DKycRequestStatus status);
    ApiResponse<UserIdDocument> getUserIdDocumentByUserId(UUID kycId);
    ApiResponse<List<UserIdDocument>> getAllUserIdDocumentsPending();
    ApiResponse<String> addAuthorizeUser(UUID userId, Role role);
    ApiResponse<String> removeAuthorizeUser(UUID userId, Role role);

}
