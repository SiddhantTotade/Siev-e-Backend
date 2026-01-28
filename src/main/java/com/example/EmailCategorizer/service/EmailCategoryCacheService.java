package com.example.EmailCategorizer.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailCategoryCacheService {

    private final StringRedisTemplate redis;

    public EmailCategoryCacheService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private static final String PREFIX = "email:category:";

    public String getCategory(String messageId) {
        return redis.opsForValue().get(PREFIX + messageId);
    }

    public void saveCategory(String messageId, String category) {
        redis.opsForValue().set(PREFIX + messageId, category);
    }

    public boolean contains(String messageId) {
        return Boolean.TRUE.equals(redis.hasKey(PREFIX + messageId));
    }
}
