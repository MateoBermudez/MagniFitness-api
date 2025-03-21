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

    public String getUrl() {
        return url;
    }

    @Value("${admin.auth.url}")
    public void setUrl(String url) {
        this.url = url;
    }
}