package com.gin.wegd.auth_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gin.wegd.auth_service.comon.UserIdDocStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Entity
@Table(name = "user_id_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "front_id", nullable = false)
    private String frontId; // Lưu đường dẫn hoặc dữ liệu Base64

    @Column(name = "back_id", nullable = false)
    private String backId; // Lưu đường dẫn hoặc dữ liệu Base64

    @Column(name = "status", nullable = false)
    private UserIdDocStatus status;
}
