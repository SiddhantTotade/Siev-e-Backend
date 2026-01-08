package com.example.EmailCategorizer.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GmailConfig {

    private static final String APPLICATION_NAME = "Email Categorizer";

    @Bean
    public Gmail gmailClient() throws Exception {
        InputStream in = getClass().getResourceAsStream("/credentials.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(List.of(GmailScopes.GMAIL_READONLY));

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)).setApplicationName("Email Categorizer").build();
    }

}
