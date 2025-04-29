package com.gin.wegd.auth_service.kafka.consumer.impl;

import com.gin.wegd.auth_service.comon.InvitationStatus;
import com.gin.wegd.auth_service.comon.InvitationType;
import com.gin.wegd.auth_service.kafka.consumer.ConsumerService;
import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.models.TransactionInvitation;
import com.gin.wegd.auth_service.models.User;
import com.gin.wegd.auth_service.repositories.TransactionInvitationRepository;
import com.gin.wegd.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final TransactionInvitationRepository transactionInvitationRepository;
    private final UserRepository userRepository;
    private final ProducerService producerService;

    @KafkaListener(topics = "transaction.request", groupId = "transaction.request.group.3")
    public void transactionInvitationEv(GenericRecord ev, Acknowledgment ack) {
        String buyerId = ev.get("BuyerId") != null ? ev.get("BuyerId").toString() : null;
        String sellerId = ev.get("SellerId") != null ? ev.get("SellerId").toString() : null;
        String transactionId = ev.get("TransactionId") != null ? ev.get("TransactionId").toString() : null;

        if (buyerId == null || sellerId == null || transactionId == null) {

            ack.acknowledge();
            return;
        }

        UUID buyerUuid;
        UUID sellerUuid;
        UUID transactionUuid;

        buyerUuid = UUID.fromString(buyerId);
        sellerUuid = UUID.fromString(sellerId);
        transactionUuid = UUID.fromString(transactionId);

        User receiver = userRepository.findById(buyerUuid).orElse(null);
        User sender = userRepository.findById(sellerUuid).orElse(null);

        if (receiver == null || sender == null) {
            ack.acknowledge();
            return;
        }

        TransactionInvitation transactionInvitation = new TransactionInvitation();
        transactionInvitation.setReceiver(receiver);
        transactionInvitation.setSender(sender);
        transactionInvitation.setTransaction(transactionUuid);
        transactionInvitation.setStatus(InvitationStatus.PENDING);
        transactionInvitation.setInvitationType(InvitationType.USER);
        transactionInvitationRepository.save(transactionInvitation);

        ack.acknowledge();
    }
}