package com.devcrew.logmicroservice.controller;

import com.devcrew.logmicroservice.dto.AppModuleDTO;
import com.devcrew.logmicroservice.service.AppModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class AppModuleController {

    private final AppModuleService appModuleService;

    @Autowired
    public AppModuleController(AppModuleService appModuleService) {
        this.appModuleService = appModuleService;
    }

    @GetMapping("/get-modules")
    public ResponseEntity<Object> getModules() {
        List<AppModuleDTO> modules = appModuleService.getModules();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/get-module/{id}")
    public ResponseEntity<Object> getModule(@PathVariable Integer id) {
        AppModuleDTO module = appModuleService.getModule(id);
        return ResponseEntity.ok(module);
    }

    @PostMapping("/save-module")
    public ResponseEntity<Object> saveModule(@RequestBody AppModuleDTO module){
        appModuleService.saveModule(module);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-module/{id}")
    public ResponseEntity<Object> deleteModule(@PathVariable Integer id) {
        appModuleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-module/{id}")
    public ResponseEntity<Object> updateModule(@PathVariable Integer id, @RequestBody String moduleName) {
        appModuleService.updateModule(id, moduleName);
        return ResponseEntity.noContent().build();
    }
}
