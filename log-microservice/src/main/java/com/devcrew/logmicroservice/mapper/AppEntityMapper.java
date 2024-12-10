package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.model.AppEntity;

public class AppEntityMapper {

    public static AppEntityDTO toDTO(AppEntity appEntity) {
        return new AppEntityDTO(appEntity.getId(), appEntity.getName());
    }

    public static AppEntity toEntity(AppEntityDTO appEntityDTO) {
        AppEntity appEntity = new AppEntity();

        if (appEntityDTO.getIdentifier() != null) {
            appEntity.setId(appEntityDTO.getIdentifier());
        }

        appEntity.setName(appEntityDTO.getName_entity());

        return appEntity;
    }
}
