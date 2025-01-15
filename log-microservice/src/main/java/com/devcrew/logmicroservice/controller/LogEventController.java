package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.dto.LogEventFilter;
import com.devcrew.logmicroservice.dto.PaginatedLogsResponse;
import com.devcrew.logmicroservice.service.LogEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller class for the LogEvent entity.
 * Handles the HTTP requests and responses.
 * Call the LogEventService to perform the business logic.
 */
@RestController
@RequestMapping("/log")
public class LogEventController {

    /**
     * The LogEventService instance to perform the business logic.
     */
    private final LogEventService logEventService;

    /**
     * Constructor for the LogEventController.
     * @param logEventService The LogEventService instance to perform the business logic.
     */
    @Autowired
    public LogEventController(LogEventService logEventService) {
        this.logEventService = logEventService;
    }

    /**
     * Get all the logs.
     * @return The list of all logs.
     */
    @GetMapping("/get-logs")
    public ResponseEntity<List<LogEventDTO>> getLogs() {
        List<LogEventDTO> logs = logEventService.getLogs();
        return ResponseEntity.ok(logs);
    }

    /**
     * Get the paginated logs with the given page number, size, filter and sort direction.
     * @param page The page number.
     * @param size The size of the page.
     * @param filter The filter to apply to the logs.
     * @param sortDirection The direction to sort the logs.
     * @return The paginated logs.
     */
    @GetMapping("/get-paginated-logs")
    public ResponseEntity<PaginatedLogsResponse> getPaginatedLogs(@RequestParam Integer page,
                                                   @RequestParam Integer size,
                                                   @RequestBody LogEventFilter filter,
                                                   @RequestParam String sortDirection) {
        PaginatedLogsResponse response = logEventService.getPaginatedLogs(page, size, filter, sortDirection);
        return ResponseEntity.ok(response);
    }

    /**
     * Get the log with the given id.
     * @param id The id of the log.
     * @return The log with the given id.
     */
    @GetMapping("/get-log/{id}")
    public ResponseEntity<LogEventDTO> getLog(@PathVariable Integer id) {
        LogEventDTO log = logEventService.getLog(id);
        return ResponseEntity.ok(log);
    }

    /**
     * Delete the log with the given id.
     * @param id The id of the log.
     * @return The response entity meaning the log is deleted.
     */
    @DeleteMapping("/delete-log/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        logEventService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all the logs.
     * @return The response entity meaning all the logs are deleted.
     */
    @DeleteMapping("/delete-logs")
    public ResponseEntity<Void> deleteLogs() {
        logEventService.deleteLogs();
        return ResponseEntity.noContent().build();
    }
}