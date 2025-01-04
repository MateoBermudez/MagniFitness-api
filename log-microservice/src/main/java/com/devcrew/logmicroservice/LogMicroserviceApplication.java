package com.devcrew.logmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This class is the main class of the LogMicroservice application.
 * It is used to run the application.
 * It is annotated with @SpringBootApplication to enable a Spring Boot application.
 * It is annotated with @EnableDiscoveryClient to enable service registration and discovery.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class LogMicroserviceApplication {

	/**
	 * This is the main method of the LogMicroservice application.
	 * It is used to run the application.
	 * @param args - the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(LogMicroserviceApplication.class, args);
	}

}
