package com.gin.wegd.auth_service.outbox;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseOutBoxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime createdAt;
    private boolean processed;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.processed = false;
    }

}
