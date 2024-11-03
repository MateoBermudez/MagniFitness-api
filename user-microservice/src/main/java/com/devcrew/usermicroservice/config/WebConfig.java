package com.devcrew.usermicroservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class that sets up the CORS mappings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds CORS mappings to the registry.
     * It allows requests from <a href="http://localhost:63342">...</a>.
     * It allows GET, POST, PUT, DELETE, and OPTIONS methods.
     *
     * @param registry the CorsRegistry instance
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //63342 is the port of IntelliJ IDEA's built-in web server
                .allowedOrigins("http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}