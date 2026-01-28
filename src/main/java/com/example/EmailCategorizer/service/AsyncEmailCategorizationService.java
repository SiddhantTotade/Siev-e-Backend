package com.example.EmailCategorizer.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.EmailCategorizer.dto.GmailDTO;

@Service
public class AsyncEmailCategorizationService {

    private final AIEmailCategorizationService ai;
    private final EmailCategoryCacheService cache;

    public AsyncEmailCategorizationService(AIEmailCategorizationService ai, EmailCategoryCacheService cache) {
        this.ai = ai;
        this.cache = cache;
    }

    @Async("aiExecutor")
    public void categorizeBatch(List<GmailDTO> emails) {
        ai.categorizeBatchSync(emails, cache);
    }
}
