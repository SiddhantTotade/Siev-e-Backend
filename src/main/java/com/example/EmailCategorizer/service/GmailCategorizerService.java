package com.example.EmailCategorizer.service;

import org.springframework.stereotype.Service;

@Service
public class GmailCategorizerService {
    public String categorize(String subject, String from) {
        return "GENERAL";
    }
}
