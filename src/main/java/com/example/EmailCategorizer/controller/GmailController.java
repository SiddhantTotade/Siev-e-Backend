package com.example.EmailCategorizer.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmailCategorizer.dto.GmailPageResponse;
import com.example.EmailCategorizer.service.GmailService;

@RestController
public class GmailController {

    private final GmailService gmailService;

    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/api/emails/me")
    public GmailPageResponse getEmails() throws IOException {
        // Hardcoding userId as "me" for Gmail API
        return gmailService.fetchEmails("me", null);
    }
}
