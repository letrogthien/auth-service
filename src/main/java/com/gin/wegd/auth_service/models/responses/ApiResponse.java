package com.gin.wegd.auth_service.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  ApiResponse<T> {
    private int status=200;
    private LocalDateTime timestamp= LocalDateTime.now();
    private String message;
    private T data;
}
