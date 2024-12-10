package com.devcrew.logmicroservice.dto;

import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ActionDTO {
    private Integer identifier;
    private String name_action;
}
