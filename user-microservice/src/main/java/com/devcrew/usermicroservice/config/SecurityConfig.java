package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.security.JwtAuthenticationFilter;
import com.devcrew.usermicroservice.service.JwtService;
import com.devcrew.usermicroservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is a configuration class that sets up the security filter chain.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * AuthenticationProvider instance to authenticate the user.
     */
    private final AuthenticationProvider authProvider;

    /**
     * Creates a SecurityFilterChain bean that configures the security filter chain to authenticate the user using JWT or OAuth2.
     *
     * @param http the HttpSecurity instance
     * @param jwtAuthenticationFilter the JwtAuthenticationFilter instance
     * @return the SecurityFilterChain instance
     * @throws Exception if an error occurs while creating the SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserService userService, JwtService jwtService,
            TwoFactorAuthFilter twoFactorAuthFilter,
            AdminFilter adminFilter,
            ApiGatewayFilter apiGatewayFilter)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/*", "/login**", "/error**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint((request, response, authException)
                                        -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(twoFactorAuthFilter, JwtAuthenticationFilter.class)
                .addFilterAfter(adminFilter, TwoFactorAuthFilter.class)
                .addFilterBefore(apiGatewayFilter, JwtAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService())
                        )
                        .successHandler(((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            AppUser user = userService.OAuth2Login(oAuth2User);
                            String token = jwtService.getToken(user);

                            Cookie jwtCookie = getJwtCookie(token);

                            response.addCookie(jwtCookie);

                            response.sendRedirect("http://localhost:4200/home");

                        }))
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"" + exception.getMessage() + "\"}");
                        })

                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                )
                .build();
    }

    private static Cookie getJwtCookie(String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        // HttpOnly flag is set false to allow the client-side to access the cookie
        jwtCookie.setHttpOnly(false);
        // Secure flag is set false to allow the cookie to be sent over HTTP (not HTTPS)
        jwtCookie.setSecure(false);
        jwtCookie.setDomain("localhost");
        jwtCookie.setPath("/home");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        return jwtCookie;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }
}