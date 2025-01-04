package com.devcrew.logmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * PaginatedLogsResponse is a DTO class used to return paginated log events.
 * It contains a page of log events and the total number of elements.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedLogsResponse {
    private Page<LogEventDTO> logs;
    private long totalElements;
}
