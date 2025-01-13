package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.mapper.AppModuleMapper;
import com.devcrew.logmicroservice.model.AppModule;
import com.devcrew.logmicroservice.repository.ModuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing modules.
 */
@Service
public class AppModuleService {

    /**
     * Module repository.
     */
    private final ModuleRepository ModuleRepository;

    /**
     * Constructor.
     * Initializes Module repository.
     * @param ModuleRepository Module repository
     */
    @Autowired
    public AppModuleService(ModuleRepository ModuleRepository) {
        this.ModuleRepository = ModuleRepository;
    }

    /**
     * Get all modules.
     * @return List of modules
     */
    @Cacheable(value = "modules")
    public List<AppModuleDTO> getModules() {
        List<AppModule> Modules = ModuleRepository.findAll();
        return Modules.stream().map(AppModuleMapper::toDTO).toList();
    }

    /**
     * Get module by id.
     * @param id Module id
     * @return Module with specified id
     */
    public AppModuleDTO getModule(Integer id) {
        AppModule Module = ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        return AppModuleMapper.toDTO(Module);
    }

    /**
     * Saves module to the database.
     * @param module Module to save
     */
    @CacheEvict(value = "modules", allEntries = true)
    @Transactional
    public void saveModule(AppModuleDTO module) {
        AppModule Module = AppModuleMapper.toEntity(module);
        ModuleRepository.save(Module);
    }

    /**
     * Deletes module from the database with specified id.
     * @param id Module id to delete
     */
    @CacheEvict(value = "modules", allEntries = true)
    @Transactional
    public void deleteModule(Integer id) {
        ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        ModuleRepository.deleteById(id);
    }

    /**
     * Updates module with specified id.
     * @param id Module id
     * @param moduleName New module name
     */
    @CacheEvict(value = "modules", allEntries = true)
    @Transactional
    public void updateModule(Integer id, String moduleName) {
        AppModule Module = ModuleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Module with id " + id + " not found")
        );
        Module.setName(moduleName);
        ModuleRepository.save(Module);
    }
}
