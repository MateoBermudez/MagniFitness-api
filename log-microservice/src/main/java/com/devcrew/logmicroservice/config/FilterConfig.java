package com.devcrew.logmicroservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public ApiGatewayFilter apiGatewayFilter() {
        return new ApiGatewayFilter();
    }

    @Bean
    public FilterRegistrationBean<ApiGatewayFilter> filterRegistration(ApiGatewayFilter apiGatewayFilter) {
        FilterRegistrationBean<ApiGatewayFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiGatewayFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}