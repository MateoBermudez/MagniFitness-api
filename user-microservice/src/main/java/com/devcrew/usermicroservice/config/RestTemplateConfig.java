package com.devcrew.usermicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig is a configuration class that creates a RestTemplate bean.
 * RestTemplate is a synchronous client to perform HTTP requests,
 * exposing a simple template method API over underlying HTTP client libraries.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean.
     *
     * @return the RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
