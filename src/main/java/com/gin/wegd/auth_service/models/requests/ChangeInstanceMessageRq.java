package com.gin.wegd.auth_service.models.requests;

import com.gin.wegd.auth_service.models.user_attribute.InstanceMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeInstanceMessageRq {
    private InstanceMessage instanceMessage;
    private String data;
}
