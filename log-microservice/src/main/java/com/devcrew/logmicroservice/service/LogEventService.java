package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.mapper.LogEventMapper;
import com.devcrew.logmicroservice.model.LogEvent;
import com.devcrew.logmicroservice.repository.LogEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LogEventService {

    private final LogEventRepository logEventRepository;

    @Autowired
    public LogEventService(LogEventRepository logEventRepository) {
        this.logEventRepository = logEventRepository;
    }

    public List<LogEventDTO> getLogs() {
        List<LogEvent> logs = logEventRepository.findAll();
        return logs.stream().map(LogEventMapper::toDTO).toList();
    }

    @Transactional
    public void saveLogEvent(String logEventJSON) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LogEvent logEvent = objectMapper.readValue(logEventJSON, LogEvent.class);
        logEventRepository.save(logEvent);
    }

    public LogEventDTO getLog(Integer id) {
        LogEvent log = logEventRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Log with id " + id + " not found")
        );
        return LogEventMapper.toDTO(log);
    }

    @Transactional
    public void deleteLog(Integer id) {
        logEventRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Log with id " + id + " not found")
        );
        logEventRepository.deleteById(id);
    }

    @Transactional
    public void deleteLogs() {
        logEventRepository.deleteAll();
    }
}
