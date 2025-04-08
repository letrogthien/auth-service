package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.models.DeleteKycRequest;

public interface DeleteKycService {
    void saveObj(DeleteKycRequest deleteKycRequest);
    void approve(DeleteKycRequest deleteKycRequest);
    void reject(DeleteKycRequest deleteKycRequest);
}
