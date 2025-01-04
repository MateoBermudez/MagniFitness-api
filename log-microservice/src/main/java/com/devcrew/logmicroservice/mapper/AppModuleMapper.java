package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.model.AppModule;

/**
 * This class is used to map the AppModule entity to the AppModuleDTO and vice versa.
 */
public class AppModuleMapper {

    /**
     * This method is used to map the AppModule entity to the AppModuleDTO.
     * @param appModule The AppModule entity.
     * @return The AppModuleDTO.
     */
    public static AppModuleDTO toDTO(AppModule appModule) {
        return new AppModuleDTO(appModule.getId(), appModule.getName());
    }

    /**
     * This method is used to map the AppModuleDTO to the AppModule entity.
     * @param appModuleDTO The AppModuleDTO.
     * @return The AppModule entity.
     */
    public static AppModule toEntity(AppModuleDTO appModuleDTO) {
        AppModule appModule = new AppModule();

        if (appModuleDTO.getIdentifier() != null) {
            appModule.setId(appModuleDTO.getIdentifier());
        }

        appModule.setName(appModuleDTO.getName_module());

        return appModule;
    }
}
