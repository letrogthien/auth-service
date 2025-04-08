package com.gin.wegd.auth_service.redis;


import com.gin.wegd.auth_service.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;
    private static final String USERNAME_KEY = "usernames";

    @PostConstruct
    public void cacheUsernames() {
        List<String> usernames = userService.getAllUserNames();
        usernames.forEach(username -> redisTemplate.opsForSet().add(USERNAME_KEY, username));
    }

    public void addUsernameToCache(String username) {
        redisTemplate.opsForSet().add(USERNAME_KEY, username);
    }

    public void removeUsernameFromCache(String username) {
        redisTemplate.opsForSet().remove(USERNAME_KEY, username);
    }

}
