package com.gin.wegd.auth_service.config;

import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        CustomException jwtException = new CustomException(ErrorCode.UNAUTHENTICATED);

        Throwable cause = authException.getCause();
        if (cause instanceof JwtException) {
            if (cause instanceof ExpiredJwtException) {
                jwtException = new CustomException(ErrorCode.EXPIRED_TOKEN);
            } else {
                jwtException = new CustomException(ErrorCode.INVALID_TOKEN);
            }
        }
        String responseBody = jwtException.toString();
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(responseBody.getBytes());
            outputStream.flush();
        }
    }
}