package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.mapper.AppEntityMapper;
import com.devcrew.logmicroservice.model.AppEntity;
import com.devcrew.logmicroservice.repository.AppEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for app entities. It is used to get, add, delete and update app entities.
 */
@Service
public class AppEntityService {

    /**
     * Repository for app entities.
     */
    private final AppEntityRepository appEntityRepository;

    /**
     * Constructor for AppEntityService.
     * @param appEntityRepository Repository for app entities.
     */
    @Autowired
    public AppEntityService(AppEntityRepository appEntityRepository) {
        this.appEntityRepository = appEntityRepository;
    }

    /**
     * Get all app entities.
     * @return List of all app entities.
     */
    @Cacheable(value = "entities")
    public List<AppEntityDTO> getEntities() {
        List<AppEntity> entities = appEntityRepository.findAll();
        return entities.stream().map(AppEntityMapper::toDTO).toList();
    }

    /**
     * Get app entity by id.
     * @param id id of the app entity.
     * @return App entity with the given id.
     */
    public AppEntityDTO getEntity(Integer id) {
        AppEntity entity = appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        return AppEntityMapper.toDTO(entity);
    }

    /**
     * Get app entity by name.
     * @param name name of the app entity.
     * @return App entity with the given name.
     */
    public AppEntityDTO getEntityByName(String name) {
        AppEntity entity = appEntityRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Entity with name " + name + " not found")
        );
        return AppEntityMapper.toDTO(entity);
    }

    /**
     * Add app entity.
     * @param entity App entity to be added.
     */
    @CacheEvict(value = "entities", allEntries = true)
    @Transactional
    public void saveEntity(AppEntityDTO entity) {
        AppEntity appEntity = AppEntityMapper.toEntity(entity);
        appEntityRepository.save(appEntity);
    }

    /**
     * Delete app entity by id.
     * @param id id of the app entity to be deleted.
     */
    @CacheEvict(value = "entities", allEntries = true)
    @Transactional
    public void deleteEntity(Integer id) {
        appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        appEntityRepository.deleteById(id);
    }

    /**
     * Update app entity.
     * @param id id of the app entity to be updated.
     * @param entityName new name of the app entity.
     */
    @CacheEvict(value = "entities", allEntries = true)
    @Transactional
    public void updateEntity(Integer id, String entityName) {
        AppEntity entity = appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        entity.setName(entityName);
        appEntityRepository.save(entity);
    }
}
