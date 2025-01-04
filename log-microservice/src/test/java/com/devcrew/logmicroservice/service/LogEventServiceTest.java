package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.model.Action;
import com.devcrew.logmicroservice.model.AppEntity;
import com.devcrew.logmicroservice.model.AppModule;
import com.devcrew.logmicroservice.model.LogEvent;
import com.devcrew.logmicroservice.repository.LogEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class LogEventServiceTest {

    private final LogEventRepository logEventRepository;

    private final LogEventService logEventService;

    @Autowired
    public LogEventServiceTest(LogEventRepository logEventRepository, LogEventService logEventService) {
        this.logEventRepository = logEventRepository;
        this.logEventService = logEventService;
    }

    @BeforeEach
    public void setUp() {
        logEventRepository.deleteAll();
        LogEvent logEvent = createLogEvent();
        logEventRepository.save(logEvent);
    }

    private LogEvent createLogEvent() {
        return new LogEvent(
                new Action(1, "Create"),
                new AppModule(1, "User"),
                new AppEntity(1, "action"),
                1,
                "user",
                "{}",
                "{}"
        );
    }

    @Test
    public void testGetLogs() {
        List<LogEventDTO> logEventDTOList = logEventService.getLogs();

        assertEquals(1, logEventDTOList.size());
        LogEventDTO logEventDTO = logEventDTOList.get(0);
        assertEquals(1, logEventDTO.getIdentifier());
        assertEquals("Create", logEventDTO.getAction().getName_action());
        assertEquals("User", logEventDTO.getAppModule().getName_module());
        assertEquals("ACTION", logEventDTO.getAppEntity().getName_entity());
        assertEquals(1, logEventDTO.getUser_identifier());
        assertEquals("user", logEventDTO.getDescription());
        assertEquals("{}", logEventDTO.getJsonBefore());
        assertEquals("{}", logEventDTO.getJsonAfter());
    }
}