package com.example.EmailCategorizer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmailCategorizer.service.EmailCategoryCacheService;

@RestController
@RequestMapping("/api/emails")
public class EmailStatusController {

    private final EmailCategoryCacheService cacheService;

    public EmailStatusController(EmailCategoryCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/status/{messageId}")
    public String getStatus(@PathVariable String messageId) {
        String category = cacheService.getCategory(messageId);
        return category != null ? category : "PENDING";
    }
}
