package com.gin.wegd.auth_service.redis;


import com.gin.wegd.auth_service.models.responses.ApiResponse;
import com.gin.wegd.auth_service.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public ApiResponse<List<String>> searchByPrefix(String keyword) {
        Set<String> usernames = redisTemplate.opsForSet().members(USERNAME_KEY);
        List<String> result = Optional.ofNullable(usernames)
                .orElse(Collections.emptySet())
                .stream()
                .filter(username -> username.contains(keyword))
                .toList();
        return ApiResponse.<List<String>>builder()
                .data(result)
                .message("Search success")
                .build();
    }

}
