package com.devcrew.logmicroservice.dto;

import lombok.*;

/**
 * Data Transfer Object for AppModule entity.
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AppModuleDTO {
    private Integer identifier;
    private String name_module;
}
