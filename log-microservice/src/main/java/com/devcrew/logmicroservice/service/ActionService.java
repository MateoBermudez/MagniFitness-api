package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.mapper.ActionMapper;
import com.devcrew.logmicroservice.model.Action;
import com.devcrew.logmicroservice.repository.ActionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for actions. It is used to get, add, delete and update actions.
 */
@Service
public class ActionService {

    /**
     * Repository for actions.
     */
    private final ActionRepository actionRepository;

    /**
     * Constructor for ActionService.
     * @param actionRepository Repository for actions.
     */
    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    /**
     * Get all actions.
     * @return List of all actions.
     */
    @Cacheable(value = "actions")
    public List<ActionDTO> getActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream().map(ActionMapper::toDTO).toList();
    }

    /**
     * Get action by id.
     * @param id id of the action.
     * @return Action with the given id.
     */
    public ActionDTO getActionById(Integer id) {
        Action action = actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action not found")
        );
        return ActionMapper.toDTO(action);
    }

    /**
     * Add action.
     * @param actionDTO Action to be added.
     */
    @CacheEvict(value = "actions", allEntries = true)
    @Transactional
    public void addAction(ActionDTO actionDTO) {
        Action action = ActionMapper.toEntity(actionDTO);
        actionRepository.save(action);
    }

    /**
     * Delete action by id.
     * @param id id of the action to be deleted.
     */
    @CacheEvict(value = "actions", allEntries = true)
    @Transactional
    public void deleteAction(Integer id) {
        actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action not found")
        );
        actionRepository.deleteById(id);
    }

    /**
     * Update action.
     * @param id id of the action to be updated.
     * @param actionName new name of the action.
     */
    @CacheEvict(value = "actions", allEntries = true)
    @Transactional
    public void updateAction(Integer id, String actionName) {
        Action action = actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action with id " + id + " not found")
        );
        action.setName(actionName);
        actionRepository.save(action);
    }
}
