package com.gin.wegd.auth_service.models.dtos;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.comon.InvitationType;
import com.gin.wegd.auth_service.models.User;
import lombok.Builder;
import lombok.Data;


import java.util.UUID;

@Data
@Builder
public class TransactionInvitationDto {

    private UUID id;
    private UUID sender;
    private UUID receiver;
    private UUID transaction;
    private InvitationType invitationType;
    private InvitationStatus status;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;


}
