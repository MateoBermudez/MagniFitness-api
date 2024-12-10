package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.ActionDTO;
import com.devcrew.logmicroservice.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping("/get-actions")
    public ResponseEntity<Object> getActions() {
        List<ActionDTO> actions = actionService.getActions();
        return ResponseEntity.ok(actions);
    }

    @GetMapping("/get-action-by-id/{id}")
    public ResponseEntity<Object> getActionById(@PathVariable Integer id) {
        ActionDTO action = actionService.getActionById(id);
        return ResponseEntity.ok(action);
    }

    @PostMapping("/add-action")
    public ResponseEntity<Object> addAction(ActionDTO actionDTO) {
        actionService.addAction(actionDTO);
        return ResponseEntity.ok("Action added successfully");
    }

    @DeleteMapping("/delete-action/{id}")
    public ResponseEntity<Object> deleteAction(@PathVariable Integer id) {
        actionService.deleteAction(id);
        return ResponseEntity.ok("Action deleted successfully");
    }

    @PutMapping("/update-action/{id}")
    public ResponseEntity<Object> updateAction(@PathVariable Integer id, @RequestBody String actionName) {
        actionService.updateAction(id, actionName);
        return ResponseEntity.noContent().build();
    }
}
