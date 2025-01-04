package com.devcrew.logmicroservice.dto;

import lombok.*;

/**
 * Data Transfer Object for Action entity
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ActionDTO {
    private Integer identifier;
    private String name_action;
}
