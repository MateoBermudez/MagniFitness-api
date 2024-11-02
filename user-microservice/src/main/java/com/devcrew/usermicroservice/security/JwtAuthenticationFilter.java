package com.devcrew.usermicroservice.security;

import com.devcrew.usermicroservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

/**
 * Filter class for JWT authentication.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Service for JWT operations.
     */
    private final JwtService jwtService;

    /**
     * Service for user details, used for loading user details from username.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Filters incoming requests and performs JWT authentication.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws IOException if an input or output exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            // Extract token from request
            final String token = getTokenFromRequest(request);
            final String username;

            // If the token is null, continue the filter chain
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Get username from token
            username = jwtService.getUsernameFromToken(token);

            // If username is not null and authentication is not set, proceed with authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate token
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Handle exceptions and write an error message to response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    /**
     * Extracts the JWT token from the request.
     *
     * @param request the HTTP request
     * @return the JWT token, or null if not found
     * @throws ServletException if an error occurs during token extraction
     */
    private String getTokenFromRequest(HttpServletRequest request) throws ServletException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Check if the authorization header contains a Bearer token
            if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return null;
        } catch (Exception e) {
            throw new ServletException("Error in JWT authentication" + e.getMessage());
        }
    }
}