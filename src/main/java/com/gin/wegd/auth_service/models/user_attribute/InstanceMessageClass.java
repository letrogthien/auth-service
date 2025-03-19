package com.gin.wegd.auth_service.models.user_attribute;

import com.gin.wegd.auth_service.models.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instance_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceMessageClass {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "instance_message")
    private InstanceMessage instanceMessage;

    @Column(name = "data")
    private String data;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}