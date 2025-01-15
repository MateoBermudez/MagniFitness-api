package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.LogMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogReceiverService {

    private final LogEventService logEventService;

    @Autowired
    public LogReceiverService(LogEventService logEventService) {
        this.logEventService = logEventService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveLog(LogMessage logMessage) {
        logEventService.saveLogEvent(logMessage);
    }
}