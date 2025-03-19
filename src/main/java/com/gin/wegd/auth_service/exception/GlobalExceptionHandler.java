package com.gin.wegd.auth_service.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handlingRuntimeEx(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handlingCustomException(CustomException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getErrorCode().getStatusCode(), e.getMessage());
        return new ResponseEntity<>(problemDetail, e.getErrorCode().getStatusCode());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ProblemDetail> handlingParseException(ParseException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ProblemDetail> handlingJwtException(JwtException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.UNAUTHORIZED);
    }
}
