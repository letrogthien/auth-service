package com.gin.wegd.auth_service.controllers;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.comon.InvitationType;
import com.gin.wegd.auth_service.models.TransactionInvitation;
import com.gin.wegd.auth_service.models.dtos.TransactionInvitationDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.TransactionInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction-invitations")
@RequiredArgsConstructor
public class TransactionInvitationController {

    private final TransactionInvitationService transactionInvitationService;

    @PostMapping("/{id}/accept")
    public ApiResponse<Void> acceptTransactionInvitation(@PathVariable UUID id) {
        transactionInvitationService.acceptTransactionInvitation(id);
        return ApiResponse.<Void>builder()
                .message("Transaction invitation accepted successfully")
                .build();
    }

    @PostMapping("/{id}/reject")
    public ApiResponse<Void> rejectTransactionInvitation(@PathVariable UUID id) {
        transactionInvitationService.rejectTransactionInvitation(id);
        return ApiResponse.<Void>builder()
                .message("Transaction invitation rejected successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionInvitationDto> getTransactionInvitationById(@PathVariable UUID id) {
        return transactionInvitationService.getTransactionInvitationById(id);
    }

    @GetMapping("/receiver/{receiverId}")
    public ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitations(@PathVariable UUID receiverId) {
        return transactionInvitationService.getAllTransactionInvitations(receiverId);
    }

    @GetMapping("/receiver/{receiverId}/status/{status}")
    public ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitationsByStatus(
            @PathVariable UUID receiverId, @PathVariable InvitationStatus status) {
        return transactionInvitationService.getAllTransactionInvitationsByStatus(receiverId, status);
    }


}