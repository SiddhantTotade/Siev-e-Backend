package com.example.EmailCategorizer.dto;

public class GmailDTO {
    private String id;
    private String from;
    private String subject;
    private String body;
    private String category;

    public GmailDTO(String id, String from, String subject, String body, String category) {
        this.id = id;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
