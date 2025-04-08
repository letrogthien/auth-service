package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.repositories.DeleteKycRepository;
import com.gin.wegd.auth_service.services.DeleteKycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DeleteKycServiceImpl implements DeleteKycService {
    private final DeleteKycRepository deleteKycRepository;

    @Override
    public void saveObj(DeleteKycRequest deleteKycRequest) {
        deleteKycRepository.save(deleteKycRequest);
    }

    @Override
    public void approve(DeleteKycRequest deleteKycRequest) {

    }

    @Override
    public void reject(DeleteKycRequest deleteKycRequest) {

    }
}
