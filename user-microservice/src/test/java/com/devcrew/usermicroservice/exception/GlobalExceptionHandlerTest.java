package com.devcrew.usermicroservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Test class for GlobalExceptionHandler.
 * This class contains tests for the global exception handling in the application.
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class GlobalExceptionHandlerTest {

    /**
     * MockMvc instance for performing HTTP requests in tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * WebApplicationContext instance for setting up the MockMvc.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Sets up the MockMvc instance before each test.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Test for handling UnauthorizedException.
     * This test verifies that the appropriate response is returned for unauthorized access.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleUnauthorizedException() throws Exception {
        mockMvc.perform(get("/api/test/unauthorized"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Unauthorized access"));
    }

    /**
     * Test for handling UserAlreadyExistsException.
     * This test verifies that the appropriate response is returned for user already exists exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleUserAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/api/test/user-already-exists"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

    /**
     * Test for handling UserDoesNotExistException.
     * This test verifies that the appropriate response is returned for user does not exist exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleUserDoesNotExistException() throws Exception {
        mockMvc.perform(get("/api/test/user-does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist"));
    }

    /**
     * Test for handling BadRequestException.
     * This test verifies that the appropriate response is returned for bad request exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleBadRequestException() throws Exception {
        mockMvc.perform(get("/api/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad request"));
    }

    /**
     * Test for handling CustomException.
     * This test verifies that the appropriate response is returned for custom exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleCustomException() throws Exception {
        mockMvc.perform(get("/api/test/custom-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Custom exception occurred"));
    }

    /**
     * Test for handling generic Exception.
     * This test verifies that the appropriate response is returned for internal server error.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleException() throws Exception {
        mockMvc.perform(get("/api/test/exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Test for handling IOException.
     * This test verifies that the appropriate response is returned for IO exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleIOException() throws Exception {
        mockMvc.perform(get("/api/test/io-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("IO exception occurred"));
    }

    /**
     * Test for handling BadCredentialsException.
     * This test verifies that the appropriate response is returned for bad credentials' exception.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testHandleBadCredentialsException() throws Exception {
        mockMvc.perform(get("/api/test/bad-credentials"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Bad credentials"));
    }
}