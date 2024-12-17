package com.devcrew.logmicroservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LogEventDTO {
    private Integer identifier;
    private String description;
    private String jsonBefore;
    private String jsonAfter;
    private ActionDTO action;
    private AppEntityDTO appEntity;
    private AppModuleDTO appModule;
    private Integer user_identifier;
    private LocalDateTime creationDate;
}
