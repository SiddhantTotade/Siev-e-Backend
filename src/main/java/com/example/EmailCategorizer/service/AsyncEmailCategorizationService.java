package com.example.EmailCategorizer.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.EmailCategorizer.dto.GmailDTO;

@Service
public class AsyncEmailCategorizationService {

    private final AIEmailCategorizationService aiService;
    private final EmailCategoryCacheService cache;

    public AsyncEmailCategorizationService(
            AIEmailCategorizationService aiService,
            EmailCategoryCacheService cache) {
        this.aiService = aiService;
        this.cache = cache;
    }

    @Async("aiExecutor")
    public void categorizeBatch(List<GmailDTO> emails) {
        aiService.categorizeBatchSync(emails, cache);
    }
}
