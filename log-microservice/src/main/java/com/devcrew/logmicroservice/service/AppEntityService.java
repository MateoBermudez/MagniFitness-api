package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.mapper.AppEntityMapper;
import com.devcrew.logmicroservice.model.AppEntity;
import com.devcrew.logmicroservice.repository.AppEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppEntityService {

    private final AppEntityRepository appEntityRepository;

    @Autowired
    public AppEntityService(AppEntityRepository appEntityRepository) {
        this.appEntityRepository = appEntityRepository;
    }

    public List<AppEntityDTO> getEntities() {
        List<AppEntity> entities = appEntityRepository.findAll();
        return entities.stream().map(AppEntityMapper::toDTO).toList();
    }

    public AppEntityDTO getEntity(Integer id) {
        AppEntity entity = appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        return AppEntityMapper.toDTO(entity);
    }

    public AppEntityDTO getEntityByName(String name) {
        AppEntity entity = appEntityRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("Entity with name " + name + " not found")
        );
        return AppEntityMapper.toDTO(entity);
    }

    @Transactional
    public void saveEntity(AppEntityDTO entity) {
        AppEntity appEntity = AppEntityMapper.toEntity(entity);
        appEntityRepository.save(appEntity);
    }

    @Transactional
    public void deleteEntity(Integer id) {
        appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        appEntityRepository.deleteById(id);
    }

    public void updateEntity(Integer id, String entityName) {
        AppEntity entity = appEntityRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Entity with id " + id + " not found")
        );
        entity.setName(entityName);
        appEntityRepository.save(entity);
    }
}
