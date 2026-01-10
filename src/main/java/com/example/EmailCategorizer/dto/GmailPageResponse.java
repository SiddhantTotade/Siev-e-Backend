package com.example.EmailCategorizer.dto;

import java.util.List;

public class GmailPageResponse {

    private List<GmailDTO> emails;
    private String nextPageToken;

    public GmailPageResponse(List<GmailDTO> emails, String nextPageToken) {
        this.emails = emails;
        this.nextPageToken = nextPageToken;
    }

    public List<GmailDTO> getEmails() {
        return emails;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }
}
