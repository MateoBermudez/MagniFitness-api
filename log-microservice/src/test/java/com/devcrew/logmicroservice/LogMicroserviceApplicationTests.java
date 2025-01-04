package com.devcrew.logmicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * This class is used to test the LogMicroservice application.
 * It is annotated with @SpringBootTest to enable a Spring Boot application context.
 */
@ActiveProfiles("test")
@SpringBootTest
class LogMicroserviceApplicationTests {

	/**
	 * This method is used to test the context loads.
	 * It should pass.
	 */
	@Test
	void contextLoads() {
	}

}
