package com.gin.wegd.auth_service.services;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.models.TransactionInvitation;
import com.gin.wegd.auth_service.models.dtos.TransactionInvitationDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionInvitationService {

    void acceptTransactionInvitation(UUID transactionInvitationId);
    void rejectTransactionInvitation(UUID transactionInvitationId);
    ApiResponse<TransactionInvitationDto> getTransactionInvitationById(UUID transactionInvitationId);
    ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitations(UUID receiverId);
    ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitationsByStatus(UUID receiverId, InvitationStatus status);
}
