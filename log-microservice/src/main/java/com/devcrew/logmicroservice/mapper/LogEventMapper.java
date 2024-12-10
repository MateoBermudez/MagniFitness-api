package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.model.LogEvent;

public class LogEventMapper {

    public static LogEventDTO toDTO(LogEvent logEvent) {
        return new LogEventDTO(
                logEvent.getId(),
                logEvent.getDescription(),
                logEvent.getJsonBefore(),
                logEvent.getJsonAfter(),
                ActionMapper.toDTO(logEvent.getActionId()),
                AppEntityMapper.toDTO(logEvent.getEntityId()),
                AppModuleMapper.toDTO(logEvent.getModuleId()),
                logEvent.getUserId(),
                logEvent.getCreationDate()
        );
    }

    public static LogEvent toEntity(LogEventDTO logEventDTO) {
        LogEvent logEvent = new LogEvent();

        if (logEventDTO.getIdentifier() != null) {
            logEvent.setId(logEventDTO.getIdentifier());
        }

        logEvent.setDescription(logEventDTO.getDescription());
        logEvent.setJsonBefore(logEventDTO.getJsonBefore());
        logEvent.setJsonAfter(logEventDTO.getJsonAfter());
        logEvent.setActionId(ActionMapper.toEntity(logEventDTO.getAction()));
        logEvent.setEntityId(AppEntityMapper.toEntity(logEventDTO.getAppEntity()));
        logEvent.setModuleId(AppModuleMapper.toEntity(logEventDTO.getAppModule()));
        logEvent.setUserId(logEventDTO.getUser_identifier());

        return logEvent;
    }
}
