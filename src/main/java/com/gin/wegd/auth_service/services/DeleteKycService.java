package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.models.DeleteKycRequest;

import java.util.List;
import java.util.UUID;

public interface DeleteKycService {
    void saveObj(DeleteKycRequest deleteKycRequest);
    void approve(UUID kycId);
    void reject(UUID kycId);
    List<DeleteKycRequest> getAllDeleteKycRequests();
    List<DeleteKycRequest> getAllDeleteKycRequestsByStatus(DKycRequestStatus status);
    DeleteKycRequest getDeleteKycRequestByUserId(UUID id);
}
