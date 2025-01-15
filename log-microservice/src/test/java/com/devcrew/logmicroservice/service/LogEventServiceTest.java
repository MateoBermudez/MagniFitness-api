package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.model.*;
import com.devcrew.logmicroservice.repository.LogEventRepository;
import com.devcrew.logmicroservice.repository.LogUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is responsible for testing the LogEventService class.
 */
@ActiveProfiles("test")
@SpringBootTest
public class LogEventServiceTest {

    /**
     * The LogEventRepository instance.
     */
    private final LogEventRepository logEventRepository;

    /**
     * The LogEventService instance.
     */
    private final LogEventService logEventService;

    /**
     * The LogUserRepository instance to access the LogUser table in the database.
     */
    private final LogUserRepository logUserRepository;

    /**
     * This is the constructor of the LogEventServiceTest class.
     * @param logEventRepository - the LogEventRepository instance
     * @param logEventService - the LogEventService instance
     */
    @Autowired
    public LogEventServiceTest(LogEventRepository logEventRepository, LogEventService logEventService, LogUserRepository logUserRepository) {
        this.logEventRepository = logEventRepository;
        this.logEventService = logEventService;
        this.logUserRepository = logUserRepository;
    }

    /**
     * This method is used to set up the test environment.
     */
    @BeforeEach
    public void setUp() {
        logEventRepository.deleteAll();
        LogEvent logEvent = createLogEvent();
        logUserRepository.save(logEvent.getUserId());
        logEventRepository.save(logEvent);
    }

    /**
     * This method is used to create a LogEvent instance.
     * @return a LogEvent instance
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
     * This method is used to test the getLogs method.
     * It should return the correct list of LogEventDTO instances.
     * The list should contain one LogEventDTO instance which was created in the setUp method.
     */
    @Test
    public void testGetLogs() {
        List<LogEventDTO> logEventDTOList = logEventService.getLogs();

        assertEquals(1, logEventDTOList.size());
        LogEventDTO logEventDTO = logEventDTOList.get(0);
        assertEquals(1, logEventDTO.getIdentifier());
        assertEquals("Create", logEventDTO.getAction().getName_action());
        assertEquals("User", logEventDTO.getAppModule().getName_module());
        assertEquals("ACTION", logEventDTO.getAppEntity().getName_entity());
        assertEquals("user", logEventDTO.getDescription());
        assertEquals("{}", logEventDTO.getJsonBefore());
        assertEquals("{}", logEventDTO.getJsonAfter());
    }
}