package com.devcrew.apigateway.filters;

import com.devcrew.apigateway.config.ServicesConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class AdminAuthFilter extends AbstractGatewayFilterFactory<AdminAuthFilter.Config> {

    private final ServicesConfig servicesConfig;

    public AdminAuthFilter(ServicesConfig servicesConfig) {
        super(AdminAuthFilter.Config.class);
        this.servicesConfig = servicesConfig;
    }

    @Override
    public GatewayFilter apply(AdminAuthFilter.Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || authHeader.isEmpty()) {
                return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing");
            }

            try {
                String jwt = authHeader.substring(7);

                Claims claims = Jwts.parser()
                        .setSigningKey(getKey())
                        .parseClaimsJws(jwt)
                        .getBody();

                if (claims.getExpiration().before(new Date())) {
                    return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "JWT token is expired");
                }

                String role = claims.get("role", String.class);

                if ("ADMIN".equals(role)) {
                    return chain.filter(exchange);
                } else {
                    return sendErrorResponse(exchange, HttpStatus.FORBIDDEN, "Access denied for role: " + role);
                }
            } catch (Exception e) {
                return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
            }
        };
    }

    private Key getKey() {
        try {
            byte[] keyBytes = servicesConfig.getJwtSecretKey().getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        return getErrorMessage(exchange, status, message);
    }

    protected static Mono<Void> getErrorMessage(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = String.format(
                "{\"error\": \"%s\", \"status\": %d}",
                message,
                status.value()
        );

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(errorBody.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }


    public static class Config {
    }
}