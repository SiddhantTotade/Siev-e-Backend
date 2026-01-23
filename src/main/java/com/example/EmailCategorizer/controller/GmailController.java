package com.example.EmailCategorizer.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmailCategorizer.dto.GmailPageResponse;
import com.example.EmailCategorizer.service.GmailService;

@RestController
@RequestMapping("/api/emails")
public class GmailController {

    private final GmailService gmailService;

    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/me")
    public GmailPageResponse getMyEmails(@RequestParam(required = false) String pageToken) throws IOException {
        return gmailService.fetchEmails("me", pageToken);
    }

}
