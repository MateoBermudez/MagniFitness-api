package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.AppEntityDTO;
import com.devcrew.logmicroservice.service.AppEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AppEntityController is a class that handles the requests from the client.
 * It is responsible for the CRUD operations of the AppEntity.
 */
@RestController
@RequestMapping("/log")
public class AppEntityController {

    /**
     * The AppEntityService object that is used to perform the CRUD operations.
     */
    private final AppEntityService appEntityService;

    /**
     * Constructor of the AppEntityController class.
     * @param appEntityService The AppEntityService object that is used to perform the CRUD operations.
     */
    @Autowired
    public AppEntityController(AppEntityService appEntityService) {
        this.appEntityService = appEntityService;
    }

    /**
     * This method is used to get all the entities.
     * @return The list of all the entities.
     */
    @GetMapping("/get-entities")
    public ResponseEntity<List<AppEntityDTO>> getEntities() {
        List<AppEntityDTO> entities = appEntityService.getEntities();
        return ResponseEntity.ok(entities);
    }

    /**
     * This method is used to get an entity by its id.
     * @param id The id of the entity.
     * @return The entity with the given id.
     */
    @GetMapping("/get-entity/{id}")
    public ResponseEntity<AppEntityDTO> getEntity(@PathVariable Integer id) {
        AppEntityDTO entity = appEntityService.getEntity(id);
        return ResponseEntity.ok(entity);
    }

    /**
     * This method is used to get an entity by its name.
     * @param name The name of the entity.
     * @return The entity with the given name.
     */
    @GetMapping("/get-entity-by-name/{name}")
    public ResponseEntity<AppEntityDTO> getEntityByName(@PathVariable String name) {
        AppEntityDTO entity = appEntityService.getEntityByName(name);
        return ResponseEntity.ok(entity);
    }

    /**
     * This method is used to save an entity.
     * @param entity The entity to be saved.
     * @return The response entity.
     */
    @PostMapping("/save-entity")
    public ResponseEntity<Void> saveEntity(@RequestBody AppEntityDTO entity){
        appEntityService.saveEntity(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This method is used to delete an entity by its id.
     * @param id The id of the entity.
     * @return The response entity.
     */
    @DeleteMapping("/delete-entity/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Integer id) {
        appEntityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to update an entity by its id.
     * @param id The id of the entity.
     * @param entityName The new name of the entity.
     * @return The response entity.
     */
    @PutMapping("/update-entity/{id}")
    public ResponseEntity<Void> updateEntity(@PathVariable Integer id, @RequestBody String entityName) {
        appEntityService.updateEntity(id, entityName);
        return ResponseEntity.noContent().build();
    }
}
