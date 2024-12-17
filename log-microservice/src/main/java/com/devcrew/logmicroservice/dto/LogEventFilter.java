package com.devcrew.logmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEventFilter {
    Integer id;
    ActionDTO action;
    AppModuleDTO module;
    AppEntityDTO entity;
    Integer userId;
    String startDate;
    String endDate;
}
