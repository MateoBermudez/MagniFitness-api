package com.devcrew.logmicroservice.mapper;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.model.AppModule;

public class AppModuleMapper {

    public static AppModuleDTO toDTO(AppModule appModule) {
        return new AppModuleDTO(appModule.getId(), appModule.getName());
    }

    public static AppModule toEntity(AppModuleDTO appModuleDTO) {
        AppModule appModule = new AppModule();

        if (appModuleDTO.getIdentifier() != null) {
            appModule.setId(appModuleDTO.getIdentifier());
        }

        appModule.setName(appModuleDTO.getName_module());

        return appModule;
    }
}
