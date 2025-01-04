package com.devcrew.usermicroservice.security;

import com.devcrew.usermicroservice.config.ApplicationConfig;
import com.devcrew.usermicroservice.config.SecurityConfig;
import com.devcrew.usermicroservice.config.UserAndPersonConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * Test class for JwtAuthenticationFilter.
 * This class contains tests for the JWT authentication filter.
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
@Import({UserAndPersonConfig.class, ApplicationConfig.class, SecurityConfig.class})
public class JwtAuthenticationFilterTest {

    /**
     * Mocked HttpServletRequest instance.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Mocked HttpServletResponse instance.
     */
    @Mock
    private HttpServletResponse response;

    /**
     * Mocked FilterChain instance.
     */
    @Mock
    private FilterChain filterChain;

    /**
     * Injected JwtAuthenticationFilter instance.
     */
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Sets up the test environment before each test.
     * Initialize mocks and clears the security context.
     *
     * @throws IOException if an input or output exception occurs
     */
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    /**
     * Test for doFilterInternal method without a token.
     * This test verifies that the filter chain continues and no authentication is set.
     *
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Test
    public void testDoFilterInternalWithoutToken() throws IOException, ServletException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}