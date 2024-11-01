package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/role-permission")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<Object> getRolePermissions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getRolePermissions(token));
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Object> getRolePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        return ResponseEntity.ok(rolePermissionService.getRolePermission(token, id));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Object> deleteRolePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        rolePermissionService.deleteRolePermission(token, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addRolePermission(@RequestHeader("Authorization") String token, @RequestBody RolePermissionDTO rolePermission) {
        rolePermissionService.addRolePermission(token, rolePermission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/update-role-permission")
    public ResponseEntity<Object> updateRolePermission(@RequestHeader("Authorization") String token, @RequestBody RolePermissionDTO rolePermission) {
        rolePermissionService.updateRolePermission(token, rolePermission);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/get-by-role/{roleId}")
    public ResponseEntity<Object> getRolePermissionsByRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        return ResponseEntity.ok(rolePermissionService.getPermissionsByRole(token, roleId));
    }

    @GetMapping(path = "/get-by-permission/{permissionId}")
    public ResponseEntity<Object> getRolePermissionsByPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        return ResponseEntity.ok(rolePermissionService.getRolesByPermission(token, permissionId));
    }

    @GetMapping(path = "get-roles")
    public ResponseEntity<Object> getRoles(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getRoles(token));
    }

    @GetMapping(path = "get-permissions")
    public ResponseEntity<Object> getPermissions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getPermissions(token));
    }

    @GetMapping(path = "/get-role/{roleId}")
    public ResponseEntity<Object> getRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        return ResponseEntity.ok(rolePermissionService.getRole(token, roleId));
    }

    @GetMapping(path = "/get-permission/{permissionId}")
    public ResponseEntity<Object> getPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        return ResponseEntity.ok(rolePermissionService.getPermission(token, permissionId));
    }

    @DeleteMapping(path = "/delete-role/{roleId}")
    public ResponseEntity<Object> deleteRolePermissionsByRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        rolePermissionService.deleteRole(token, roleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/delete-permission/{permissionId}")
    public ResponseEntity<Object> deleteRolePermissionsByPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        rolePermissionService.deletePermission(token, permissionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/add-role")
    public ResponseEntity<Object> addRole(@RequestHeader("Authorization") String token, @RequestBody RoleDTO role) {
        rolePermissionService.addRole(token, role);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/add-permission")
    public ResponseEntity<Object> addPermission(@RequestHeader("Authorization") String token, @RequestBody PermissionDTO permission) {
        rolePermissionService.addPermission(token, permission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/update-role")
    public ResponseEntity<Object> updateRole(@RequestHeader("Authorization") String token, @RequestBody RoleDTO role) {
        rolePermissionService.updateRole(token, role);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/update-permission")
    public ResponseEntity<Object> updatePermission(@RequestHeader("Authorization") String token, @RequestBody PermissionDTO permission) {
        rolePermissionService.updatePermission(token, permission);
        return ResponseEntity.noContent().build();
    }
}