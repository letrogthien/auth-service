package com.gin.wegd.auth_service.kafka.topics;

import lombok.Getter;

@Getter
public enum AuthTopic {
    PASSWORD_RESET("auth.ev.reset-password"),
    OTP("auth.ev.otp"),
    REGISTER("auth.ev.register"),
    NOTIFY_EMAIL("auth.ev.base-notify"),
    REJECT_TRANSACTION_INVITATION("auth.ev.reject-transaction-invitation"),
    ACCEPT_TRANSACTION_INVITATION("auth.ev.accept-transaction-invitation");

    private final String name;

    AuthTopic(String name) {
        this.name = name;
    }
}
