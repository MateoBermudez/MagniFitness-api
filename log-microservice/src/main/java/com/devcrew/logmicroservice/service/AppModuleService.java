package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.mapper.AppModuleMapper;
import com.devcrew.logmicroservice.model.AppModule;
import com.devcrew.logmicroservice.repository.ModuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppModuleService {

    private final ModuleRepository ModuleRepository;

    @Autowired
    public AppModuleService(ModuleRepository ModuleRepository) {
        this.ModuleRepository = ModuleRepository;
    }

    public List<AppModuleDTO> getModules() {
        List<AppModule> Modules = ModuleRepository.findAll();
        return Modules.stream().map(AppModuleMapper::toDTO).toList();
    }

    public AppModuleDTO getModule(Integer id) {
        AppModule Module = ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        return AppModuleMapper.toDTO(Module);
    }

    @Transactional
    public void saveModule(AppModuleDTO module) {
        AppModule Module = AppModuleMapper.toEntity(module);
        ModuleRepository.save(Module);
    }

    @Transactional
    public void deleteModule(Integer id) {
        ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        ModuleRepository.deleteById(id);
    }

    public void updateModule(Integer id, String moduleName) {
        AppModule Module = ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        Module.setName(moduleName);
        ModuleRepository.save(Module);
    }
}
