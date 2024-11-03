package com.devcrew.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The ApiGatewayApplication class is the main class of the API Gateway service.
 * It is a Spring Boot application.
 * It is used to run the API Gateway service.
 * It is annotated with @SpringBootApplication to enable a Spring Boot application.
 * It is annotated with @EnableDiscoveryClient to enable service registration and discovery.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	/**
	 * The main method is the entry point of the API Gateway service.
	 * It is used to run the API Gateway service.
	 * @param args An array of strings which stores arguments passed by the user.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
