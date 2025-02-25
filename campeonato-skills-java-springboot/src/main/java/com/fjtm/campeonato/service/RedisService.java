package com.fjtm.campeonato.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenInvalid(String token) {
        return redisTemplate.hasKey(token);
    }

    public void invalidateToken(String token) {
        redisTemplate.delete(token);
    }

    public boolean isTokenStored(String jwt) {
        return redisTemplate.hasKey(jwt);
    }
}


