package com.gin.wegd.auth_service.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    USER_ALREADY_BANNED(400, "User is already banned", HttpStatus.BAD_REQUEST),
    USER_NOT_BANNED(400, "User is not banned", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(409, "User existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(409, "Email existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "You need to log in to perform this action.", HttpStatus.UNAUTHORIZED),
    USERNAME_OR_PASSWORD_FAIL(401, "Username or password incorrect", HttpStatus.UNAUTHORIZED),
    COURSER_NOT_EXISTED(400, "Course not existed", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(401, "INVALID_TOKEN", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(401, "EXPIRED_TOKEN", HttpStatus.UNAUTHORIZED),
    CURRENT_PASSWORD_INVALID(400, "Current password is invalid", HttpStatus.BAD_REQUEST),
    OTP_WRONG(401,"OTP wrong", HttpStatus.UNAUTHORIZED),
    OTP_EXPIRED(401,"OTP expired", HttpStatus.UNAUTHORIZED),
    OTP_WRONG_USER(401, "UnAuth userId",HttpStatus.UNAUTHORIZED ),
    OTP_INVALID_PURPOSE(404,"invalid otp purpose" ,HttpStatus.BAD_REQUEST ),
    BLOCK_REQUEST(400,"request is block",HttpStatus.BAD_REQUEST),
    KAFKA_SEND_FAILED(500, "Failed to send reset password event to Kafka", HttpStatus.INTERNAL_SERVER_ERROR),
    EXECUTOR_SUBMISSION_FAILED(500, "Failed to submit task to executor", HttpStatus.INTERNAL_SERVER_ERROR);



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
