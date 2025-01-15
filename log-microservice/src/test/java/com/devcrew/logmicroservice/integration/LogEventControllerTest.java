package com.devcrew.logmicroservice.integration;

import com.devcrew.logmicroservice.model.*;
import com.devcrew.logmicroservice.repository.LogEventRepository;
import com.devcrew.logmicroservice.repository.LogUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class is responsible for testing the LogEventController class.
 * It is annotated with @SpringBootTest to enable a Spring Boot application context.
 */
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class LogEventControllerTest {

    /**
     * The MockMvc object is used to perform HTTP requests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The LogEventRepository object is used to access the LogEvent table in the database.
     */
    @Autowired
    private LogEventRepository logEventRepository;

    /**
     * This is the internal API key used to authenticate requests coming from the API Gateway.
     */
    @Value("${internal.api.key}")
    private String internalApiKey;

    /**
     * The LogUserRepository object is used to access the LogUser table in the database.
     */
    @Autowired
    private LogUserRepository logUserRepository;

    /**
     * This method is used to set up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        logEventRepository.deleteAll();
        LogEvent logEvent = createLogEvent();
        logUserRepository.save(logEvent.getUserId());
        logEventRepository.save(logEvent);
    }

    /**
     * This method is used to create a LogEvent object.
     * @return a LogEvent object
     */
    private LogEvent createLogEvent() {
        return new LogEvent(
                new Action(1, "Create"),
                new AppModule(1, "User"),
                new AppEntity(1, "action"),
                new LogUser(1, "user", "hola@mail.com"),
                "user",
                "{}",
                "{}"
        );
    }

    /**
     * This method is used to test the getLogs method of the LogEventController class.
     * It should return a list of LogEvent objects.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetLogs() throws Exception {
        mockMvc.perform(get("/log/get-logs")
                        .header("X-API-Key", internalApiKey)
                        .contentType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(jakarta.ws.rs.core.MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].identifier").exists())
                .andExpect(jsonPath("$[0].action.identifier").exists())
                .andExpect(jsonPath("$[0].appModule.identifier").exists())
                .andExpect(jsonPath("$[0].appEntity.identifier").exists());
    }
}