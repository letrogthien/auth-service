package com.gin.wegd.auth_service.models;

import com.gin.wegd.auth_service.comon.Role;
import com.gin.wegd.auth_service.models.user_attribute.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private InstanceMessageClass instanceMessage;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserPhone phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "2fa_enabled")
    private boolean twoFactorAuthEnabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserIdDocument userIdDocument;
}