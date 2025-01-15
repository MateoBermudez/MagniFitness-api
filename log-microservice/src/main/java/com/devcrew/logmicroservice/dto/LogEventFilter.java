package com.devcrew.logmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LogEventFilter is a DTO class used to filter log events.
 * If an attribute is null, it is not used in the filter.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEventFilter {
    Integer id;
    ActionDTO action;
    AppModuleDTO module;
    AppEntityDTO entity;
    LogUserDTO userId;
    String startDate;
    String endDate;
}
