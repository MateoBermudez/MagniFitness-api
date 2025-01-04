package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.model.LogEvent;

/**
 * This class is used to map the LogEvent entity to the LogEventDTO and vice versa.
 */
public class LogEventMapper {

    /**
     * This method is used to map the LogEvent entity to the LogEventDTO.
     * @param logEvent The LogEvent entity.
     * @return The LogEventDTO.
     */
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

    /**
     * This method is used to map the LogEventDTO to the LogEvent entity.
     * It currently has no use in the project.
     * Because the LogEvent is being created by a JSON object.
     * @param logEventDTO The LogEventDTO.
     * @return The LogEvent entity.
     */
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
