package com.gin.wegd.auth_service.exception;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
