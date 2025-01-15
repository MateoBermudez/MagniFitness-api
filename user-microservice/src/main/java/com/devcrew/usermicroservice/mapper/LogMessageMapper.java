package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.LogMessage;

public class LogMessageMapper {
    public static LogMessage toLogMessage(Integer actionId,
                                          Integer moduleId,
                                          Integer entityId,
                                          String action,
                                          String module,
                                          String entity,
                                          Integer userId,
                                          String description,
                                          String jsonBefore,
                                          String jsonAfter,
                                          String username,
                                          String email) {
        return new LogMessage(actionId, moduleId, entityId, action, module, entity, userId, username, email, description, jsonBefore, jsonAfter);
    }
}
