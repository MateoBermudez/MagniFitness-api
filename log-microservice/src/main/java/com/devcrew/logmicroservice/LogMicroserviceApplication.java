package com.devcrew.logmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LogMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogMicroserviceApplication.class, args);
	}

}
