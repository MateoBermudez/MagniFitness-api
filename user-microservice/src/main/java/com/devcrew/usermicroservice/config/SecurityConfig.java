package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.security.JwtAuthenticationFilter;
import com.devcrew.usermicroservice.service.JwtService;
import com.devcrew.usermicroservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
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
     * Creates a SecurityFilterChain bean that configures the security filter chain to authenticate the user using JWT.
     *
     * @param http the HttpSecurity instance
     * @param jwtAuthenticationFilter the JwtAuthenticationFilter instance
     * @return the SecurityFilterChain instance
     * @throws Exception if an error occurs while creating the SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService, JwtService jwtService) throws Exception {
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
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService())
                        )
//                        .loginPage("https://frontend-server.com/login")
//                        .defaultSuccessUrl("https://frontend-server.com/home", true)
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
//                        .failureUrl("https://frontend-server.com/login?error=true")
                        .successHandler(((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            AppUser user = userService.saveOAuth2User(oAuth2User);
                            String token = jwtService.getToken(user);
                            response.addHeader("Authorization", "Bearer " + token);
                        }))
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                )
                .build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }
}