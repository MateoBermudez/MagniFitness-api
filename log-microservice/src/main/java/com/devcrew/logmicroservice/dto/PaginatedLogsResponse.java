package com.devcrew.logmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedLogsResponse {
    private Page<LogEventDTO> logs;
    private long totalElements;
}
