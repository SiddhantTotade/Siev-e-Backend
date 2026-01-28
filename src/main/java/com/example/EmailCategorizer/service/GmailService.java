package com.example.EmailCategorizer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.EmailCategorizer.dto.GmailDTO;
import com.example.EmailCategorizer.dto.GmailPageResponse;
import com.google.api.services.gmail.Gmail;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class GmailService {

    private final Gmail gmail;
    private final EmailCategoryCacheService cache;
    private final AsyncEmailCategorizationService asyncAI;

    public GmailService(
            Gmail gmail,
            EmailCategoryCacheService cache,
            AsyncEmailCategorizationService asyncAI) {
        this.gmail = gmail;
        this.cache = cache;
        this.asyncAI = asyncAI;
    }

    @RateLimiter(name = "gmail")
    public GmailPageResponse fetchEmails(String userId, String pageToken) throws IOException {

        var response = gmail.users()
                .messages()
                .list(userId)
                .setMaxResults(10L)
                .setPageToken(pageToken)
                .execute();

        List<GmailDTO> emails = new ArrayList<>();
        List<GmailDTO> toCategorize = new ArrayList<>();

        if (response.getMessages() == null) {
            return new GmailPageResponse(emails, response.getNextPageToken());
        }

        for (var msg : response.getMessages()) {

            var full = gmail.users().messages().get(userId, msg.getId()).execute();

            String from = "";
            String subject = "";

            for (var h : full.getPayload().getHeaders()) {
                if ("From".equalsIgnoreCase(h.getName()))
                    from = h.getValue();
                if ("Subject".equalsIgnoreCase(h.getName()))
                    subject = h.getValue();
            }

            String body = full.getSnippet();
            String id = full.getId();

            String category = cache.getCategory(id);

            GmailDTO dto = new GmailDTO(
                    id,
                    from,
                    subject,
                    body,
                    category != null ? category : "PROCESSING");

            if (category == null) {
                cache.saveCategory(id, "PROCESSING");
                toCategorize.add(dto);
            }

            emails.add(dto);
        }

        if (!toCategorize.isEmpty()) {
            asyncAI.categorizeBatch(toCategorize);
        }

        return new GmailPageResponse(emails, response.getNextPageToken());
    }
}
