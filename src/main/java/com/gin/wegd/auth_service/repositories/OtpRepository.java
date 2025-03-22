package com.gin.wegd.auth_service.repositories;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.models.OtpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface OtpRepository extends JpaRepository<OtpModel, UUID> {

    Optional<OtpModel> findByOtp(String otp);

    OtpModel otp(String otp);
    @Query("""
            SELECT o FROM OtpModel o
            WHERE o.userId = ?1
            AND o.otp = ?2
            AND o.otpPurpose = ?3
            AND o.active = true
            AND o.expiredAt > ?4
            """)
    Optional<OtpModel> findValidOtp(UUID userId, String otp, OtpPurpose otpPurpose, LocalDateTime now);

}
