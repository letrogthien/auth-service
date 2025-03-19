package com.gin.wegd.auth_service.models.user_attribute;

import com.gin.wegd.auth_service.models.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_phone")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPhone {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "active")
    private boolean active;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}