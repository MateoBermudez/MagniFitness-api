package com.devcrew.logmicroservice.service;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.mapper.ActionMapper;
import com.devcrew.logmicroservice.model.Action;
import com.devcrew.logmicroservice.repository.ActionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService {

    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<ActionDTO> getActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream().map(ActionMapper::toDTO).toList();
    }

    public ActionDTO getActionById(Integer id) {
        Action action = actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action not found")
        );
        return ActionMapper.toDTO(action);
    }

    @Transactional
    public void addAction(ActionDTO actionDTO) {
        Action action = ActionMapper.toEntity(actionDTO);
        actionRepository.save(action);
    }

    @Transactional
    public void deleteAction(Integer id) {
        actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action not found")
        );
        actionRepository.deleteById(id);
    }

    public void updateAction(Integer id, String actionName) {
        Action action = actionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Action with id " + id + " not found")
        );
        action.setName(actionName);
        actionRepository.save(action);
    }
}
