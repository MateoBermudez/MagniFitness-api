package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.service.AppEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class AppEntityController {

    private final AppEntityService appEntityService;

    @Autowired
    public AppEntityController(AppEntityService appEntityService) {
        this.appEntityService = appEntityService;
    }

    @GetMapping("/get-entities")
    public ResponseEntity<Object> getEntities() {
        List<AppEntityDTO> entities = appEntityService.getEntities();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/get-entity/{id}")
    public ResponseEntity<Object> getEntity(@PathVariable Integer id) {
        AppEntityDTO entity = appEntityService.getEntity(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/get-entity-by-name/{name}")
    public ResponseEntity<Object> getEntityByName(@PathVariable String name) {
        AppEntityDTO entity = appEntityService.getEntityByName(name);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/save-entity")
    public ResponseEntity<Object> saveEntity(@RequestBody AppEntityDTO entity){
        appEntityService.saveEntity(entity);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-entity/{id}")
    public ResponseEntity<Object> deleteEntity(@PathVariable Integer id) {
        appEntityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-entity/{id}")
    public ResponseEntity<Object> updateEntity(@PathVariable Integer id, @RequestBody String entityName) {
        appEntityService.updateEntity(id, entityName);
        return ResponseEntity.noContent().build();
    }
}
