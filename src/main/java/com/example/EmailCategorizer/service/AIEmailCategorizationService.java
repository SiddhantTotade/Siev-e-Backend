package com.example.EmailCategorizer.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIEmailCategorizationService {

    private static final String AI_SERVICE_URL = "http://localhost:8000/categorize";

    private final RestTemplate restTemplate = new RestTemplate();

    @Async("aiExecutor")
    public CompletableFuture<String> categorizeEmailWithAi(String subject, String body) {

        Map<String, String> request = Map.of(
                "subject", subject,
                "body", body);

        try {
            ResponseEntity<Map<String, String>> response =
                    restTemplate.exchange(
                            AI_SERVICE_URL,
                            HttpMethod.POST,
                            new org.springframework.http.HttpEntity<>(request),
                            new ParameterizedTypeReference<Map<String, String>>() {
                            });

            return CompletableFuture.completedFuture(
                    response.getBody().getOrDefault("category", "OTHER"));

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("OTHER");
        }
    }
}
