package com.gin.wegd.auth_service.redis;

import com.gin.wegd.auth_service.models.StrangeDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StrangeDeviceCacheService {
    private final RedisTemplate<String, StrangeDevice> redisTemplate;

    public void addStrangeDevice(UUID userId, StrangeDevice strangeDevice) {
        String key = generateKey(userId);
        redisTemplate.opsForSet().add(key, strangeDevice);
        redisTemplate.expire(key, 30, TimeUnit.DAYS);
    }

    public boolean isStrangeDevice(UUID userId, StrangeDevice strangeDevice) {
        String key = generateKey(userId);
        Set<StrangeDevice> strangeDevices = redisTemplate.opsForSet().members(key);
        if (strangeDevices == null || strangeDevices.isEmpty()){
            return false;
        }
        return !strangeDevices.contains(strangeDevice);
    }


    public String generateKey(UUID userId) {
        return "device:" + userId;
    }


}
