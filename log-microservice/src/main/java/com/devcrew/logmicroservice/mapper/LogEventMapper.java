package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.LogEventDTO;
import com.devcrew.logmicroservice.dto.LogMessage;
import com.devcrew.logmicroservice.model.*;

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
                LogUserMapper.toLogUserDTO(logEvent.getUserId()),
                logEvent.getCreationDate()
        );
    }

    /**
     * This method is used to map the LogEventDTO to the LogEvent entity.
     * It currently has no use in the project.
     * Because the LogEvent is being created by a JSON object.
     * @param logMessage The LogMessage object. Received from RabbitMQ.
     * @return The LogEvent entity.
     */
    public static LogEvent toEntity(LogMessage logMessage) {
        LogEvent logEvent = new LogEvent();

        Action action = new Action();
        action.setId(logMessage.getActionId());
        action.setName(logMessage.getAction());
        logEvent.setActionId(action);

        AppModule module = new AppModule();
        module.setId(logMessage.getModuleId());
        module.setName(logMessage.getModule());
        logEvent.setModuleId(module);

        AppEntity entity = new AppEntity();
        entity.setId(logMessage.getEntityId());
        entity.setName(logMessage.getEntity());
        logEvent.setEntityId(entity);

        LogUser user = new LogUser();
        user.setId(logMessage.getUserId());
        user.setUsername(logMessage.getUsername());
        user.setEmail(logMessage.getEmail());
        logEvent.setUserId(user);

        logEvent.setJsonBefore(logMessage.getJsonBefore());
        logEvent.setJsonAfter(logMessage.getJsonAfter());
        logEvent.setDescription(logMessage.getDescription());

        return logEvent;
    }
}
