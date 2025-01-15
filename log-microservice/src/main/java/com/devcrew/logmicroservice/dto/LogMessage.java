package com.devcrew.logmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer actionId;
    private Integer moduleId;
    private Integer entityId;
    private String action;
    private String module;
    private String entity;
    private Integer userId;
    private String username;
    private String email;
    private String description;
    private String jsonBefore;
    private String jsonAfter;
}