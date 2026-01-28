package com.example.EmailCategorizer.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.EmailCategorizer.dto.AIBatchEmail;
import com.example.EmailCategorizer.dto.AIBatchRequest;
import com.example.EmailCategorizer.dto.GmailDTO;

@Service
public class AIEmailCategorizationService {

    private static final String AI_URL = "http://192.168.1.21:8000/categorize/batch";

    private final RestTemplate restTemplate = new RestTemplate();

    public void categorizeBatchSync(List<GmailDTO> emails, EmailCategoryCacheService cache) {

        List<AIBatchEmail> batch = emails.stream()
                .map(e -> new AIBatchEmail(e.getId(), e.getSubject(), e.getBody()))
                .toList();

        AIBatchRequest request = new AIBatchRequest(batch);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AIBatchRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map<String, List<Map<String, Object>>>> response = restTemplate.exchange(
                    AI_URL,
                    HttpMethod.POST,
                    entity,
                    new org.springframework.core.ParameterizedTypeReference<>() {}
            );

            for (var r : response.getBody().get("results")) {
                cache.saveCategory((String) r.get("messageId"), (String) r.get("category"));
            }

        } catch (Exception e) {
            emails.forEach(em -> cache.saveCategory(em.getId(), "OTHER"));
        }
    }
}
