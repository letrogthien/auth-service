package com.gin.wegd.auth_service.kafka.producer.impl;


import com.gin.wegd.auth_service.kafka.producer.ProducerService;
import com.gin.wegd.auth_service.kafka.topics.AuthTopic;
import com.gin.wegd.auth_service.models.TransactionInvitationEv;
import com.gin.wegd.common.events.BaseNotifyEmail;
import com.gin.wegd.common.events.ForgotPasswordEvModel;
import com.gin.wegd.common.events.OtpEvModel;
import com.gin.wegd.common.events.RegisterEvModel;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void registerEv(RegisterEvModel ev) {

        kafkaTemplate.send(AuthTopic.REGISTER.getName(), ev);
    }


    @Override
    public void sendOtpEv(OtpEvModel ev) {
        kafkaTemplate.send(AuthTopic.OTP.getName(), ev);
    }

    @Override
    public void forgotPasswordEv(ForgotPasswordEvModel ev) {
        kafkaTemplate.send(AuthTopic.PASSWORD_RESET.getName(), ev);
    }

    @Override
    public void baseNotifyEv(BaseNotifyEmail ev) {
        kafkaTemplate.send(AuthTopic.NOTIFY_EMAIL.getName(), ev);
    }

    @Override
    public void rejectTransactionInvitationEv(TransactionInvitationEv ev) {
        kafkaTemplate.send(AuthTopic.REJECT_TRANSACTION_INVITATION.getName(), ev);
    }

    @Override
    public void acceptTransactionInvitationEv(TransactionInvitationEv ev) {
        kafkaTemplate.send(AuthTopic.ACCEPT_TRANSACTION_INVITATION.getName(), ev);
    }
}
