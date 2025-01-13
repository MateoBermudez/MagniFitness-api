package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is a controller class that handles the requests and responses of the actions.
 */
@RestController
@RequestMapping("/log")
public class ActionController {

    /**
     * The action service that is used to handle the actions.
     */
    private final ActionService actionService;

    /**
     * The constructor of the action controller.
     * @param actionService The action service that is used to handle the actions.
     */
    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * This method is used to get all the actions.
     * @return The list of all the actions.
     */
    @GetMapping("/get-actions")
    public ResponseEntity<List<ActionDTO>> getActions() {
        List<ActionDTO> actions = actionService.getActions();
        return ResponseEntity.ok(actions);
    }

    /**
     * This method is used to get an action by its id.
     * @param id The id of the action.
     * @return The action with the given id.
     */
    @GetMapping("/get-action-by-id/{id}")
    public ResponseEntity<ActionDTO> getActionById(@PathVariable Integer id) {
        ActionDTO action = actionService.getActionById(id);
        return ResponseEntity.ok(action);
    }

    /**
     * This method is used to add an action.
     * @param actionDTO The action that is going to be added.
     * @return The response entity.
     */
    @PostMapping("/add-action")
    public ResponseEntity<Void> addAction(ActionDTO actionDTO) {
        actionService.addAction(actionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This method is used to delete an action by its id.
     * @param id The id of the action.
     * @return The response entity.
     */
    @DeleteMapping("/delete-action/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Integer id) {
        actionService.deleteAction(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to update an action by its id.
     * @param id The id of the action.
     * @param actionName The new name of the action.
     * @return The response entity.
     */
    @PutMapping("/update-action/{id}")
    public ResponseEntity<Void> updateAction(@PathVariable Integer id, @RequestBody String actionName) {
        actionService.updateAction(id, actionName);
        return ResponseEntity.noContent().build();
    }
}
