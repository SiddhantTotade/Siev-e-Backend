package com.example.EmailCategorizer.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncEmailCategorizationService {

    private final AIEmailCategorizationService aiService;
    private final EmailCategoryCacheService cacheService;

    public AsyncEmailCategorizationService(
            AIEmailCategorizationService aiService,
            EmailCategoryCacheService cacheService) {
        this.aiService = aiService;
        this.cacheService = cacheService;
    }

    @Async
    public void categorizeAndCacheEmail(String messageId, String subject, String body) {

        aiService.categorizeEmailWithAi(subject, body)
                .thenAccept(category -> cacheService.saveCategory(messageId, category));
    }
}
