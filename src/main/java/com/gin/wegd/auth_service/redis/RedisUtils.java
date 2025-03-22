package com.gin.wegd.auth_service.redis;


import com.gin.wegd.auth_service.comon.TokenStatus;
import com.gin.wegd.auth_service.exception.CustomException;
import com.gin.wegd.auth_service.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;

    public void setRefreshToken(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }
    public boolean refreshTokenIsInactive(String key) {
        String status = stringRedisTemplate.opsForValue().get(key);
        return status != null && status.equals(TokenStatus.INACTIVE.toString());
    }

    public void invalidateUserTokens(String userTokenPattern) {
        Set<String> keys = stringRedisTemplate.keys(userTokenPattern);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    public boolean isRefreshTokenActive(String key) {
        String status = stringRedisTemplate.opsForValue().get(key);
        return status != null && status.equals(TokenStatus.ACTIVE.toString());
    }
}
