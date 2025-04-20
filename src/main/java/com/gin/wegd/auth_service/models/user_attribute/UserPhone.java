package com.gin.wegd.auth_service.models.user_attribute;

import com.gin.wegd.auth_service.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_phone")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "active")
    private boolean active;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}