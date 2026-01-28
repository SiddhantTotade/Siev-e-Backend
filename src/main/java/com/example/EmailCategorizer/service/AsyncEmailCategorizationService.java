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

    // Use aiExecutor defined in AsyncConfig
    @Async("aiExecutor")
    public void categorizeBatch(List<GmailDTO> emails) {
        System.out.println("🔥 ASYNC THREAD Started: " + Thread.currentThread().getName());
        ai.categorizeBatchSync(emails, cache);
        System.out.println("🔥 ASYNC THREAD Ended");
    }
}
