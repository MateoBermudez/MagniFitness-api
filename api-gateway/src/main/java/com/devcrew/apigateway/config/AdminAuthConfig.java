package com.devcrew.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the admin authentication.
 * This class is used to store the URL of the admin authentication service.
 */
@Configuration
public class AdminAuthConfig {
    @Value("${admin.auth.url}")
    private String url;

    @Value("${internal.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public String getUrl() {
        return url;
    }

    @Value("${admin.auth.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${internal.api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}