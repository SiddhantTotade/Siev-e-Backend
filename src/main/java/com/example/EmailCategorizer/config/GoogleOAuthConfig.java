package com.example.EmailCategorizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleOAuthConfig {
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClienSecret() {
        return clientSecret;
    }

}
