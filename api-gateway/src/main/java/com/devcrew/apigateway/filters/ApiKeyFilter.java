package com.devcrew.apigateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for filtering requests based on the API key.
 * It checks if the API key in the request header matches the internal API key.
 * It works for the log microservice.
 * Because the log microservice is an internal service, only accessed by admins.
 */
@Component
public class ApiKeyFilter extends AbstractGatewayFilterFactory<ApiKeyFilter.Config> {

    @Value("${internal.api.key}")
    private String internalApiKey;

    public ApiKeyFilter() {
        super(Config.class);
    }

    public static class Config {
        // No configuration properties needed
    }

    /**
     * This method checks if the API key in the request header matches the internal API key.
     * @param config Configuration properties
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String apiKey = headers.getFirst("Internal-Api-Key");

            if (apiKey != null && internalApiKey.trim().equals(apiKey.trim())) {
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        };
    }
}