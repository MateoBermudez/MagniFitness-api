package com.devcrew.usermicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for the UserMicroserviceApplication.
 * The test checks if the application context loads without any exceptions.
 */
@ActiveProfiles("test")
@SpringBootTest
class UserMicroserviceApplicationTests {

	/**
	 * Test the context loading of the application.
	 * The application should start without any exceptions.
	 */
	@Test
	void contextLoads() {
	}

}
