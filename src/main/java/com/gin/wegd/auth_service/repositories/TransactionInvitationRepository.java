package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.comon.InvitationType;
import com.gin.wegd.auth_service.models.TransactionInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionInvitationRepository extends JpaRepository<TransactionInvitation, UUID> {

    List<TransactionInvitation> findAllByReceiverId(UUID receiverId);

    List<TransactionInvitation> findAllByReceiverIdAndStatus(UUID receiverId, InvitationStatus status);

    List<TransactionInvitation> findAllByInvitationType(InvitationType invitationType);

    List<TransactionInvitation> findAllByInvitationTypeAndStatus(InvitationType invitationType, InvitationStatus status);
}
