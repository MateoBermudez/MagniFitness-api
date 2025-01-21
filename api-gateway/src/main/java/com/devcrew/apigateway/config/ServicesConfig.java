package com.devcrew.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the admin authentication.
 * It contains the configuration for the internal API key and the JWT secret key.
 */
@Configuration
public class ServicesConfig {

    @Value("${internal.api.key}")
    private String apiKey;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Value("${internal.api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Value("${jwt.secret}")
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }
}