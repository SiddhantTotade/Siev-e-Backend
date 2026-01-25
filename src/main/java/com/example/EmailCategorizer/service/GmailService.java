package com.example.EmailCategorizer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.EmailCategorizer.dto.GmailDTO;
import com.example.EmailCategorizer.dto.GmailPageResponse;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

@Service
public class GmailService {

    private final Gmail gmail;
    private final EmailCategoryCacheService cacheService;
    private final AIEmailCategorizationService aiService; // synchronous AI service

    public GmailService(Gmail gmail,
                        EmailCategoryCacheService cacheService,
                        AIEmailCategorizationService aiService) {
        this.gmail = gmail;
        this.cacheService = cacheService;
        this.aiService = aiService;
    }

    public GmailPageResponse fetchEmails(String userId, String pageToken) throws IOException {

        if (gmail == null) {
            return new GmailPageResponse(new ArrayList<>(), null);
        }

        // Fetch Gmail messages
        ListMessagesResponse response = gmail.users()
                .messages()
                .list(userId)
                .setMaxResults(10L)
                .setPageToken(pageToken)
                .execute();

        List<GmailDTO> emails = new ArrayList<>();
        List<GmailDTO> uncategorized = new ArrayList<>();

        if (response.getMessages() != null) {
            for (Message msg : response.getMessages()) {

                Message fullMessage = gmail.users().messages().get(userId, msg.getId()).execute();

                String from = "";
                String subject = "";
                for (var header : fullMessage.getPayload().getHeaders()) {
                    if ("From".equalsIgnoreCase(header.getName()))
                        from = header.getValue();
                    if ("Subject".equalsIgnoreCase(header.getName()))
                        subject = header.getValue();
                }

                String snippet = fullMessage.getSnippet();
                String messageId = fullMessage.getId();

                // Check cache
                String category = cacheService.getCategory(messageId);
                if (category == null) {
                    // mark as uncategorized
                    GmailDTO emailDTO = new GmailDTO(messageId, from, subject, snippet, null);
                    uncategorized.add(emailDTO);
                    emails.add(emailDTO); // placeholder, will update category after AI call
                } else {
                    emails.add(new GmailDTO(messageId, from, subject, snippet, category));
                }
            }
        }

        // Call AI microservice for uncategorized emails synchronously
        if (!uncategorized.isEmpty()) {
            aiService.categorizeBatchSync(uncategorized, cacheService); // blocks until categories are saved

            // Update categories in original list
            for (GmailDTO email : emails) {
                if (email.getCategory() == null) {
                    email.setCategory(cacheService.getCategory(email.getId()));
                }
            }
        }

        return new GmailPageResponse(emails, response.getNextPageToken());
    }
}
