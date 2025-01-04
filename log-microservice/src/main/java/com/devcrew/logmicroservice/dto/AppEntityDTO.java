package com.devcrew.logmicroservice.dto;

import lombok.*;

/**
 * Data Transfer Object for AppEntity entity.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AppEntityDTO {
    private Integer identifier;
    private String name_entity;
}
