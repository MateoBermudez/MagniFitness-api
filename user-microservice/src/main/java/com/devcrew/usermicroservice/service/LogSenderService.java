package com.devcrew.usermicroservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LogSenderService {

    private final RestTemplate restTemplate;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Value("${log.service.url}")
    private String url;

    public void sendLog(Integer actionId,
                        Integer moduleId,
                        Integer entityId,
                        String action,
                        String module,
                        String entity,
                        Integer userId,
                        String description,
                        String jsonBefore,
                        String jsonAfter) {
        String json = String.format("""
                {
                    "actionId": {
                        "id": %d,
                        "name": "%s"
                    },
                    "moduleId": {
                        "id": %d,
                        "name": "%s"
                    },
                    "entityId": {
                        "id": %d,
                        "name": "%s"
                    },
                    "userId": %d,
                    "description": "%s",
                    "jsonBefore": "%s",
                    "jsonAfter": "%s"
                }
                """, actionId, action, moduleId, module, entityId, entity, userId, description, jsonBefore, jsonAfter);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Internal-Api-Key", internalApiKey);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
        restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }
}
