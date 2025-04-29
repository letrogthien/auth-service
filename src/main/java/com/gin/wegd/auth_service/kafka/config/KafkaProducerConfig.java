package com.gin.wegd.auth_service.kafka.config;

import com.gin.wegd.auth_service.kafka.topics.AuthTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaProducerConfig {

    @Bean
    NewTopic forgetPasswordTopic() {
        return new NewTopic(AuthTopic.PASSWORD_RESET.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic registerTopic() {
        return new NewTopic(AuthTopic.REGISTER.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic sendOtpTopic() {
        return new NewTopic(AuthTopic.OTP.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic baseNotifyEmailTopic() {
        return new NewTopic(AuthTopic.NOTIFY_EMAIL.getName(), 1, (short) 1);
    }

    @Bean
    NewTopic rejectTransactionInvitationTopic() {
        return new NewTopic(AuthTopic.REJECT_TRANSACTION_INVITATION.getName(), 1, (short) 1);
    }
    @Bean
    NewTopic acceptTransactionInvitationTopic() {
        return new NewTopic(AuthTopic.ACCEPT_TRANSACTION_INVITATION.getName(), 1, (short) 1);
    }
}
