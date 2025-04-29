package com.gin.wegd.auth_service.kafka.producer;


import com.gin.wegd.auth_service.models.TransactionInvitationEv;
import com.gin.wegd.common.events.BaseNotifyEmail;
import com.gin.wegd.common.events.ForgotPasswordEvModel;
import com.gin.wegd.common.events.OtpEvModel;
import com.gin.wegd.common.events.RegisterEvModel;

public interface ProducerService {
    void registerEv(RegisterEvModel ev);
    void sendOtpEv(OtpEvModel ev);
    void forgotPasswordEv(ForgotPasswordEvModel ev);
    void baseNotifyEv(BaseNotifyEmail ev);
    void rejectTransactionInvitationEv(TransactionInvitationEv ev);
    void acceptTransactionInvitationEv(TransactionInvitationEv ev);
}
