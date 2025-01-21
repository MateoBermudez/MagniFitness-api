package com.devcrew.apigateway.filters;

import com.devcrew.apigateway.config.ServicesConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.devcrew.apigateway.filters.AdminAuthFilter.getErrorMessage;

@Component
public class UserAuthFilter extends AbstractGatewayFilterFactory<UserAuthFilter.Config> {

    private final ServicesConfig servicesConfig;

    public UserAuthFilter(ServicesConfig servicesConfig) {
        super(Config.class);
        this.servicesConfig = servicesConfig;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || authHeader.isEmpty()) {
                return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing");
            }

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(getKey())
                        .parseClaimsJws(authHeader.replace("Bearer ", ""))
                        .getBody();

                if (claims.getExpiration().before(new Date())) {
                    return sendErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "JWT token is expired");
                }

                String role = claims.get("role", String.class);

                if ("USER".equals(role) || "ADMIN".equals(role)) {
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


    public static class Config {
    }
}