package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.LogUserDTO;
import com.devcrew.logmicroservice.model.LogUser;

public class LogUserMapper  {
    public static LogUserDTO toLogUserDTO(LogUser logUser) {
        return LogUserDTO.builder()
                .id(logUser.getId())
                .username(logUser.getUsername())
                .email(logUser.getEmail())
                .build();
    }

    public static LogUser toLogUser(LogUserDTO logUserDTO) {
        return new LogUser(
                logUserDTO.getId(),
                logUserDTO.getUsername(),
                logUserDTO.getEmail()
        );
    }
}
