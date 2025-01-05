package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.service.AppModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AppModuleController is a RestController class that provides endpoints for AppModule operations.
 */
@RestController
@RequestMapping("/log")
public class AppModuleController {

    /**
     * AppModuleService object to perform AppModule operations.
     */
    private final AppModuleService appModuleService;

    /**
     * AppModuleController constructor that initializes AppModuleService object.
     * @param appModuleService AppModuleService object to perform AppModule operations.
     */
    @Autowired
    public AppModuleController(AppModuleService appModuleService) {
        this.appModuleService = appModuleService;
    }

    /**
     * getModules method returns all modules in the database.
     * @return ResponseEntity object with all modules.
     */
    @GetMapping("/get-modules")
    public ResponseEntity<List<AppModuleDTO>> getModules() {
        List<AppModuleDTO> modules = appModuleService.getModules();
        return ResponseEntity.ok(modules);
    }

    /**
     * getModule method returns a module with the given id.
     * @param id Integer value of the module id.
     * @return ResponseEntity object with the module.
     */
    @GetMapping("/get-module/{id}")
    public ResponseEntity<AppModuleDTO> getModule(@PathVariable Integer id) {
        AppModuleDTO module = appModuleService.getModule(id);
        return ResponseEntity.ok(module);
    }

    /**
     * saveModule method saves the given module to the database.
     * @param module AppModuleDTO object to be saved.
     * @return ResponseEntity object with no content.
     */
    @PostMapping("/save-module")
    public ResponseEntity<Void> saveModule(@RequestBody AppModuleDTO module){
        appModuleService.saveModule(module);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * deleteModule method deletes the module with the given id.
     * @param id Integer value of the module id.
     * @return ResponseEntity object with no content.
     */
    @DeleteMapping("/delete-module/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Integer id) {
        appModuleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * updateModule method updates the module with the given id.
     * @param id Integer value of the module id.
     * @param moduleName String value of the new module name.
     * @return ResponseEntity object with no content, meaning the module is updated.
     */
    @PutMapping("/update-module/{id}")
    public ResponseEntity<Void> updateModule(@PathVariable Integer id, @RequestBody String moduleName) {
        appModuleService.updateModule(id, moduleName);
        return ResponseEntity.noContent().build();
    }
}
