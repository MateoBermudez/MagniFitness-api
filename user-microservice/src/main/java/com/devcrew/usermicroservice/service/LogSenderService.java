package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.LogMessage;
import com.devcrew.usermicroservice.mapper.LogMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogSenderService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void mapAndSendLog(Integer actionId,
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
        LogMessage logMessage = LogMessageMapper.toLogMessage(actionId, moduleId, entityId, action, module, entity, userId, description, jsonBefore, jsonAfter, username, email);

        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, logMessage);
        } catch (Exception e) {
            System.err.println("Error sending log message: " + e.getMessage());
        }
    }
}