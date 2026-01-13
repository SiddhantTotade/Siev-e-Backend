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
    private final AsyncEmailCategorizationService asyncCategorizationService;

    public GmailService(
            Gmail gmail,
            EmailCategoryCacheService cacheService,
            AsyncEmailCategorizationService asyncCategorizationService) {
        this.gmail = gmail;
        this.cacheService = cacheService;
        this.asyncCategorizationService = asyncCategorizationService;
    }

    public GmailPageResponse fetchEmails(String userId, String pageToken) throws IOException {

        ListMessagesResponse response = gmail.users()
                .messages()
                .list(userId)
                .setMaxResults(10L)
                .setPageToken(pageToken)
                .execute();

        List<GmailDTO> emails = new ArrayList<>();

        if (response.getMessages() != null) {
            for (Message msg : response.getMessages()) {

                Message fullMessage = gmail.users().messages().get(userId, msg.getId()).execute();

                String from = "";
                String subject = "";

                for (var header : fullMessage.getPayload().getHeaders()) {
                    if ("From".equalsIgnoreCase(header.getName())) {
                        from = header.getValue();
                    }
                    if ("Subject".equalsIgnoreCase(header.getName())) {
                        subject = header.getValue();
                    }
                }

                String snippet = fullMessage.getSnippet();
                String messageId = fullMessage.getId();

                String category = cacheService.getCategory(messageId);
                if (category == null) {
                    asyncCategorizationService.categorizeAndCacheEmail(
                            messageId,
                            subject,
                            snippet);
                    category = "PENDING"; // AI will categorize asynchronously
                }

                emails.add(new GmailDTO(
                        messageId,
                        from,
                        subject,
                        snippet,
                        category));
            }
        }

        return new GmailPageResponse(emails, response.getNextPageToken());
    }
}
