package com.devcrew.logmicroservice.dto;

import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AppModuleDTO {
    private Integer identifier;
    private String name_module;
}
