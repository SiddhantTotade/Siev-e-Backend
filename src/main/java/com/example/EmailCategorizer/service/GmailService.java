package com.example.EmailCategorizer.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

@Service
public class GmailService {

    private final Gmail gmail;

    public GmailService(Gmail gmailClient) {
        this.gmail = gmailClient;
    }

    public List<Message> fetchEmails(String userId) throws IOException {
        ListMessagesResponse response = gmail.users().messages().list(userId).execute();
        return response.getMessages();
    }
}
