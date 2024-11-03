package com.devcrew.usermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * Main class for the User Microservice, is where the application starts running
 * {@code @EnableDiscoveryClient} annotation is used
 * to make the microservice discoverable by the Eureka server and the api-gateway
 * {@code @SpringBootApplication} the annotation is used
 * to enable the autoconfiguration feature of the Spring Boot framework
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserMicroserviceApplication {


	/**
	 * The Main method of the User Microservice is where the application starts running
	 * @param args the command line arguments passed to the application when it is started,
	 * the arguments are used to configure the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceApplication.class, args);
	}
}