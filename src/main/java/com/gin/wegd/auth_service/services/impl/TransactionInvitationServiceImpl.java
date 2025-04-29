package com.gin.wegd.auth_service.services.impl;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.TransactionInvitation;
import com.gin.wegd.auth_service.models.TransactionInvitationEv;
import com.gin.wegd.auth_service.models.dtos.TransactionInvitationDto;
import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.repositories.TransactionInvitationRepository;
import com.gin.wegd.auth_service.services.TransactionInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionInvitationServiceImpl implements TransactionInvitationService {
    private final TransactionInvitationRepository tranIRepository;
    private final ProducerService producerService;

    public TransactionInvitation getById(UUID id)
    {
        return tranIRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Transaction invitation not found")
        );
    }

    @Override
    public void acceptTransactionInvitation(UUID transactionInvitationId) {
        TransactionInvitation t = this.getById(transactionInvitationId);
        UUID userId = extractUserIdInContext();

        if (!userId.equals(t.getReceiver().getId())){
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        t.setStatus(InvitationStatus.ACCEPTED);
        tranIRepository.save(t);
        producerService.acceptTransactionInvitationEv(
                TransactionInvitationEv.newBuilder()
                        .setTransaction(t.getTransaction())
                        .setId(t.getId())
                        .setBuyerId(t.getReceiver().getId())
                        .setSellerId(t.getSender().getId())
                        .setInvitationType(t.getInvitationType().toString())
                        .setStatus(t.getStatus().toString())
                        .build()
        );
    }

    @Override
    public void rejectTransactionInvitation(UUID transactionInvitationId) {
        TransactionInvitation t = this.getById(transactionInvitationId);
        UUID userId = extractUserIdInContext();
        if (!userId.equals(t.getReceiver().getId())){
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        t.setStatus(InvitationStatus.REJECTED);
        tranIRepository.save(t);
        producerService.rejectTransactionInvitationEv(
                TransactionInvitationEv.newBuilder()
                        .setTransaction(t.getTransaction())
                        .setId(t.getId())
                        .setBuyerId(t.getReceiver().getId())
                        .setSellerId(t.getSender().getId())
                        .setInvitationType(t.getInvitationType().toString())
                        .setStatus(t.getStatus().toString())
                        .build()
        );
    }

    @Override
    public ApiResponse<TransactionInvitationDto> getTransactionInvitationById(UUID transactionInvitationId) {
        TransactionInvitation t1 = this.getById(transactionInvitationId);
        TransactionInvitationDto t = TransactionInvitationDto.builder()
                .id(t1.getId())
                .sender(t1.getSender().getId())
                .receiver(t1.getReceiver().getId())
                .transaction(t1.getTransaction())
                .status(t1.getStatus())
                .invitationType(t1.getInvitationType())
                .build();
        UUID userId = extractUserIdInContext();
        if (userId != t.getReceiver()){
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        return ApiResponse.<TransactionInvitationDto>builder()
                .data(t)
                .message("Transaction invitation retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitations(UUID receiverId) {
        List<TransactionInvitationDto> transactionInvitations = tranIRepository.findAllByReceiverId(receiverId).stream()
                .map(t ->
                    TransactionInvitationDto.builder()
                            .id(t.getId())
                            .sender(t.getSender().getId())
                            .receiver(t.getReceiver().getId())
                            .transaction(t.getTransaction())
                            .status(t.getStatus())
                            .invitationType(t.getInvitationType())
                            .build()
                )
                .toList();
        if (transactionInvitations.isEmpty()) {
            return ApiResponse.<List<TransactionInvitationDto>>builder()
                    .message("No transaction invitations found")
                    .build();
        }
        return ApiResponse.<List<TransactionInvitationDto>>builder()
                .data(transactionInvitations)
                .message("Transaction invitations retrieved successfully")
                .build();
    }

    @Override
    public ApiResponse<List<TransactionInvitationDto>> getAllTransactionInvitationsByStatus(UUID receiverId, InvitationStatus status) {
        List<TransactionInvitationDto> transactionInvitations = tranIRepository.findAllByReceiverIdAndStatus(receiverId, status)
                .stream().map(t ->
                    TransactionInvitationDto.builder()
                            .id(t.getId())
                            .sender(t.getSender().getId())
                            .receiver(t.getReceiver().getId())
                            .transaction(t.getTransaction())
                            .status(t.getStatus())
                            .invitationType(t.getInvitationType())
                            .build()
                ).toList();
        if (transactionInvitations.isEmpty()) {
            return ApiResponse.<List<TransactionInvitationDto>>builder()
                    .message("No transaction invitations found with the specified status")
                    .build();
        }
        return ApiResponse.<List<TransactionInvitationDto>>builder()
                .data(transactionInvitations)
                .message("Transaction invitations retrieved successfully")
                .build();
    }

    private UUID extractUserIdInContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt);
        return UUID.fromString(jwt.getClaim("id"));
    }




}
