package com.devcrew.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * The ApiGatewayApplicationTests class is responsible for testing the API Gateway service.
 * It is annotated with @SpringBootTest to indicate that the tests are Spring Boot tests.
 */
@ActiveProfiles("test")
@SpringBootTest
class ApiGatewayApplicationTests {

	/**
	 * The contextLoads method is used to test it if the context loads successfully.
	 * It is annotated with @Test to indicate that it is a test method.
	 * It should pass if the context loads successfully, otherwise it should fail.
	 * The application should start without any errors.
	 */
	@Test
	void contextLoads() {
	}

}
