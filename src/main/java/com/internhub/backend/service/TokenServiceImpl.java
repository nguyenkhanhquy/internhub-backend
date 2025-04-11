package com.internhub.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public TokenServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void saveInvalidatedToken(String tokenId, Date expiryTime) {
//        long ttlInMillis = expiryTime.getTime() - System.currentTimeMillis();

        Instant expiryInstant = expiryTime.toInstant();
        long ttlInMillis = Duration.between(Instant.now(), expiryInstant).toMillis();

        if (ttlInMillis > 0) {
            stringRedisTemplate.opsForValue().set(tokenId, "invalid", Duration.ofMillis(ttlInMillis));
        } else {
            log.warn("Token {} đã hết hạn, không lưu vào Redis.", tokenId);
        }
    }

    @Override
    public boolean isTokenInvalidated(String tokenId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(tokenId));
    }
}
