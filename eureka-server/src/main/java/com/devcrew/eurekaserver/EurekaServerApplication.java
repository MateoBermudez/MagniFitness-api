package com.devcrew.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaServerApplication class
 * This class is the main class of the Eureka Server
 * It is used to start the Eureka Server
 * It is annotated with @SpringBootApplication and @EnableEurekaServer
 * {@code @SpringBootApplication} is used to enable Spring Boot features
 * {@code @EnableEurekaServer} is used to enable Eureka Server features in the application
 * Eureka Server is a service registry that allows microservices to register themselves and discover other services
 * It is used to enable service discovery in a microservices architecture
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    /**
     * The Main method
     * This method is used to start the Eureka Server
     * It creates a new Spring Application and runs it
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
