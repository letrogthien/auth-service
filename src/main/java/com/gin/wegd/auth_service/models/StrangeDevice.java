package com.gin.wegd.auth_service.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StrangeDevice {
    private String deviceName;
    private String deviceType;
}
