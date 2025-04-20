package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.DKycRequestStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.models.DeleteKycRequest;
import com.gin.wegd.auth_service.repositories.DeleteKycRepository;
import com.gin.wegd.auth_service.services.DeleteKycService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class DeleteKycServiceImpl implements DeleteKycService {
    private final DeleteKycRepository deleteKycRepository;

    @Override
    public void saveObj(DeleteKycRequest deleteKycRequest) {
        deleteKycRepository.save(deleteKycRequest);
    }

    @Override
    public void approve(UUID kycId) {
        DeleteKycRequest deleteKycRequest = deleteKycRepository.findById(kycId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        deleteKycRequest.setStatus(DKycRequestStatus.APPROVED);
        deleteKycRepository.save(deleteKycRequest);
    }

    @Override
    public void reject(UUID kycId) {
        DeleteKycRequest deleteKycRequest = deleteKycRepository.findById(kycId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        deleteKycRequest.setStatus(DKycRequestStatus.REJECTED);
        deleteKycRepository.save(deleteKycRequest);
    }

    @Override
    public DeleteKycRequest getDeleteKycRequestByUserId(UUID id) {
        return deleteKycRepository.findByUserId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    @Override
    public List<DeleteKycRequest> getAllDeleteKycRequests() {
        return deleteKycRepository.findAll();
    }

    @Override
    public List<DeleteKycRequest> getAllDeleteKycRequestsByStatus(DKycRequestStatus status) {
        return deleteKycRepository.findAllByStatus(status);
    }

}
