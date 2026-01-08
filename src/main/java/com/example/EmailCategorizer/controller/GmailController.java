package com.example.EmailCategorizer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmailCategorizer.service.GmailService;
import com.google.api.services.gmail.model.Message;

@RestController
public class GmailController {

    private final GmailService gmailService;

    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/emails")
    public List<Message> getEmails() throws IOException {
        return gmailService.fetchEmails("me");
    }
}
