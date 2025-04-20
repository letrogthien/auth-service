package com.gin.wegd.auth_service.models.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAddressRq {
    private String address1;

    private String address2;

    private String city;

    private String country;
}
