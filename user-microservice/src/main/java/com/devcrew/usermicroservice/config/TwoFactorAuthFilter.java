package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

/**
 * TwoFactorAuthFilter is a filter that performs two-factor authentication.
 * If the user has two-factor authentication enabled, the filter checks if the user is logged in.
 * If the user is not logged in, the filter returns a 403 Forbidden response.
 * If the user is logged in, the filter continues the filter chain.
 */
@Component
public class TwoFactorAuthFilter implements Filter {

    private final JwtValidation jwtValidation;
    private final UserRepository userRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludedPaths = List.of(
            "/auth/**",
            "/login**",
            "/error**",
            "/2fa/**"
    );


    @Autowired
    public TwoFactorAuthFilter(JwtValidation jwtValidation, UserRepository userRepository) {
        this.jwtValidation = jwtValidation;
        this.userRepository = userRepository;
    }

    /**
     * Filters incoming requests and performs two-factor authentication.
     * If the user has two-factor authentication enabled, the filter checks if the user is logged in.
     * If the user is not logged in, the filter returns a 403 Forbidden response.
     * If the user is logged in, the filter continues the filter chain.
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        boolean isExcludedPath = isExcludedPath(requestURI);

        if (isExcludedPath) {
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ServletException("Authorization header is missing");
        }

        String username = jwtValidation.validateUsernameFromToken(token);
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User with username " + username + " does not exist"));

        if (!user.hasTwoFactorAuth()) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"2FA_REQUIRED\"}");
            httpResponse.sendRedirect("http://localhost:4200/2fa");
            return;
        }

        if (!user.getLoggedIn() || !user.getEnabled()) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"ACCESS_FORBIDDEN\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Checks if the path is excluded from two-factor authentication.
     * @param path the path to check
     * @return true if the path is excluded, false otherwise
     */
    private boolean isExcludedPath(String path) {
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization, if needed
    }

    @Override
    public void destroy() {
        // Filter destruction, if needed
    }
}