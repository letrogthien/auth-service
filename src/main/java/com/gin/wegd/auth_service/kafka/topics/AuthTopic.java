package com.gin.wegd.auth_service.kafka.topics;

import lombok.Getter;

@Getter
public enum AuthTopic {
    PASSWORD_RESET("auth.ev.reset-password"),
    OTP("auth.ev.otp"),
    REGISTER("auth.ev.register"),
    DELETE("auth.ev.delete"), NOTIFY_EMAIL("auth.ev.base-notify");

    private final String name;

    AuthTopic(String name) {
        this.name = name;
    }
}
