package com.example.EmailCategorizer.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.EmailCategorizer.dto.AIBatchEmail;
import com.example.EmailCategorizer.dto.AIBatchRequest;
import com.example.EmailCategorizer.dto.GmailDTO;

@Service
public class AIEmailCategorizationService {

    private static final String AI_URL = "http://localhost:8000/categorize/batch";

    private final RestTemplate restTemplate = new RestTemplate();

    public void categorizeBatchSync(List<GmailDTO> emails, EmailCategoryCacheService cache) {
        List<AIBatchEmail> batch = emails.stream()
                .map(e -> new AIBatchEmail(e.getId(), e.getSubject(), e.getBody()))
                .toList();

        AIBatchRequest request = new AIBatchRequest(batch);

        try {
            ResponseEntity<Map<String, List<Map<String, Object>>>> response = restTemplate.exchange(
                    AI_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<>() {
                    });

            for (var r : response.getBody().get("results")) {
                cache.saveCategory((String) r.get("messageId"), (String) r.get("category"));
            }

        } catch (Exception e) {
            emails.forEach(email -> cache.saveCategory(email.getId(), "OTHER"));
        }
    }

}
