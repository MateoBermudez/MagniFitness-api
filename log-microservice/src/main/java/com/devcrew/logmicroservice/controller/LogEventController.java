package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.service.LogEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogEventController {

    private final LogEventService logEventService;

    @Autowired
    public LogEventController(LogEventService logEventService) {
        this.logEventService = logEventService;
    }

    @GetMapping("/get-logs")
    public ResponseEntity<Object> getLogs() {
        List<LogEventDTO> logs = logEventService.getLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/get-paginated-logs")
    public ResponseEntity<Object> getPaginatedLogs(@RequestParam Integer page,
                                                   @RequestParam Integer size,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate,
                                                   @RequestParam String sortDirection) {
        Page<LogEventDTO> logs = logEventService.getPaginatedLogs(page, size, startDate, endDate, sortDirection);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/get-log/{id}")
    public ResponseEntity<Object> getLog(@PathVariable Integer id) {
        LogEventDTO log = logEventService.getLog(id);
        return ResponseEntity.ok(log);
    }

    // Throws exception because of the JSON reading class
    @PostMapping("/save-log")
    public ResponseEntity<Object> saveLog(@RequestBody String logEventJSON) throws Exception {
        logEventService.saveLogEvent(logEventJSON);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-log/{id}")
    public ResponseEntity<Object> deleteLog(@PathVariable Integer id) {
        logEventService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-logs")
    public ResponseEntity<Object> deleteLogs() {
        logEventService.deleteLogs();
        return ResponseEntity.noContent().build();
    }
}