package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.model.AppEntity;

/**
 * Mapper class for AppEntity and AppEntityDTO
 */
public class AppEntityMapper {

    /**
     * This method is used to map the AppEntity entity to the AppEntityDTO.
     * @param appEntity The AppEntity entity.
     * @return The AppEntityDTO.
     */
    public static AppEntityDTO toDTO(AppEntity appEntity) {
        return new AppEntityDTO(appEntity.getId(), appEntity.getName());
    }

    /**
     * This method is used to map the AppEntityDTO to the AppEntity entity.
     * @param appEntityDTO The AppEntityDTO.
     * @return The AppEntity entity.
     */
    public static AppEntity toEntity(AppEntityDTO appEntityDTO) {
        AppEntity appEntity = new AppEntity();

        if (appEntityDTO.getIdentifier() != null) {
            appEntity.setId(appEntityDTO.getIdentifier());
        }

        appEntity.setName(appEntityDTO.getName_entity());

        return appEntity;
    }
}
