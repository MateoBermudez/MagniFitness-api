package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.AuthorizationUtils;
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

@Component
public class AdminFilter implements Filter {

    private final JwtValidation jwtValidation;
    private final UserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // List of paths that require admin permissions
    private final List<String> includedPaths = List.of(
            "/api/role-permission/**",
            "/api/person/get-all",
            "/api/person/delete/**",
            "/api/user/get-all",
            "/api/user/changeRole/**",
            "/log/**"
    );

    @Autowired
    public AdminFilter(JwtValidation jwtValidation, UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        this.jwtValidation = jwtValidation;
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        boolean isIncludedPath = isIncludedPath(requestURI);

        if (!isIncludedPath) {
            // If the path is not included, we let the request pass through
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ServletException("Authorization header is missing");
        }

        // Check if this try-catch block is necessary
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
        } catch (Exception e) {
            System.err.println("Error validating admin permissions: " + e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\": \"ACCESS_FORBIDDEN. YOU DO NOT HAVE PERMISSION TO ACCESS THIS RESOURCE" + e.getMessage() + "\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isIncludedPath(String path) {
        return includedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
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
