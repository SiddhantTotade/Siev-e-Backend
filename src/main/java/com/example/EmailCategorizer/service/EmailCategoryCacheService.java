package com.example.EmailCategorizer.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class EmailCategoryCacheService {

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public String getCategory(String messageId) {
        return cache.get(messageId);
    }

    public void saveCategory(String messageId, String category) {
        cache.put(messageId, category);
    }

    public boolean contains(String messageId) {
        return cache.containsKey(messageId);
    }
}
