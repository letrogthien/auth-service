package com.gin.wegd.auth_service.models.dtos;




import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.comon.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenDto {
    private String token;
    private UUID userId;
    private TokenStatus status;
    private TokenType tokenType;
}
