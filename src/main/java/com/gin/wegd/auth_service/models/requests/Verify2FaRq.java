package com.gin.wegd.auth_service.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Verify2FaRq {
    String token;
    String otp;
}
