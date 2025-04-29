package com.gin.wegd.auth_service.kafka.consumer;


import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface ConsumerService {
    public void transactionInvitationEv(GenericRecord ev, Acknowledgment ack);
}