package com.gin.wegd.auth_service.redis;


import com.gin.wegd.auth_service.models.JwtModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RedisConf {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("token", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)));
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
    }

    @Bean
    RedisTemplate<String, String> redisTemplateString(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    @Bean
    public RedisTemplate<String, JwtModel> redisTemplateJwt(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, JwtModel> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(JwtModel.class));
        return template;
    }
}
