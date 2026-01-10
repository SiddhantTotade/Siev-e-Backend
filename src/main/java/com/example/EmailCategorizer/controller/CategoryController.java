package com.example.EmailCategorizer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmailCategorizer.service.EmailCategoryCacheService;

@RestController
public class CategoryController {

    private final EmailCategoryCacheService cacheService;

    public CategoryController(EmailCategoryCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/api/category/{messageId}")
    public String getCategory(@PathVariable String messageId) {

        String category = cacheService.getCategory(messageId);
        return category != null ? category : "PENDING";
    }
}
