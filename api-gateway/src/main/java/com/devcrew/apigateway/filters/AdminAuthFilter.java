package com.devcrew.apigateway.filters;

import com.devcrew.apigateway.config.AdminAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * This filter is responsible for authenticating the admin user.
 * It sends a request to the user microservice to check if the user is an admin.
 * If the user is an admin, the request is forwarded to the target service.
 */
@Component
public class AdminAuthFilter extends AbstractGatewayFilterFactory<AdminAuthFilter.Config> {

    private final RestTemplate restTemplate;

    private final AdminAuthConfig adminAuthConfig;


    @Autowired
    public AdminAuthFilter(RestTemplate restTemplate, AdminAuthConfig adminAuthConfig) {
        super(Config.class);
        this.restTemplate = restTemplate;
        this.adminAuthConfig = adminAuthConfig;
    }

    @Configuration
    public static class Config {
        // No configuration needed
    }

    /**
     * This method is called when the filter is applied.
     * It checks if the user is an admin by sending a request to the user microservice.
     * If the user is an admin, the request is forwarded to the target service.
     * If the user is not an admin, a 403 Forbidden response is returned.
     * @param config The configuration of the filter
     * @return The filter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                boolean isAdmin = authenticateAdmin(exchange, config);
                if (!isAdmin) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap("{\"error\": \"Unauthorized access. You are not an admin.\"}".getBytes());
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                }
                return chain.filter(exchange);
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(("{\"error\": \"" + e.getMessage() + "\"}").getBytes());
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
        };
    }

    /**
     * This method sends a request to the user microservice to check if the user is an admin.
     * @param exchange The server web exchange
     * @param config The configuration of the filter
     * @return True if the user is an admin, false otherwise
     */
    private boolean authenticateAdmin(ServerWebExchange exchange, Config config) {
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    adminAuthConfig.getUrl(),
                    HttpMethod.GET,
                    entity,
                    Boolean.class
            );
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to authenticate admin: " + e.getMessage(), e);
        }
    }
}