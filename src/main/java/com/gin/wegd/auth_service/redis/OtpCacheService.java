package com.gin.wegd.auth_service.redis;

import com.gin.wegd.auth_service.comon.OtpPurpose;
import com.gin.wegd.auth_service.models.OtpModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Optional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpCacheService {
    private final RedisTemplate<String, OtpModel> redisTemplate;
    public Optional<OtpModel> findValidOtp(UUID userId, String otp, OtpPurpose otpPurpose) {
        String key = generateKey(userId, otpPurpose);
        OtpModel otpModel = redisTemplate.opsForValue().get(key);

        if (otpModel != null && otpModel.getOtp().equals(otp) ) {
            return Optional.of(otpModel);
        }
        return Optional.empty();
    }

    public void save(OtpModel otpModel) {
        String key = generateKey(otpModel.getUserId(), otpModel.getOtpPurpose());
        redisTemplate.opsForValue().set(key, otpModel, 5, TimeUnit.MINUTES); // Set expiration to 5 minutes
    }

    public void deleteOtp(String key) {
        redisTemplate.delete(key);
    }



    public String generateKey(UUID userId, OtpPurpose otpPurpose) {
        return "otp:" + userId + ":" + otpPurpose.name();
    }


}
