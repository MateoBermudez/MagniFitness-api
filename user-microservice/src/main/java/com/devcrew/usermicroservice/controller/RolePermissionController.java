package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is the controller class for the RolePermission entity.
 * It contains the endpoints for the RolePermission entity, Role entity, and Permission entity.
 * The endpoints are used to get, add, update, and delete a role-permission, role, and permission.
 * The endpoints are secured with the @PreAuthorize annotation to restrict access to the endpoints.
 * The endpoints are secured based on the role of the user.
 * This can only be accessed by admin users.
 */
@RestController
@RequestMapping(path = "api/role-permission")
public class RolePermissionController {

    /**
     * The role permission service is used to perform operations on the role-permission service:
     * (RolePermission entity, Role entity, and Permission entity).
     */
    private final RolePermissionService rolePermissionService;

    /**
     * The constructor is used to inject the role permission service into the role permission controller.
     * @param rolePermissionService The role permission service to be injected into the role permission controller.
     */
    @Autowired
    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    /**
     * This endpoint is used to get all the role-permissions in the system.
     *
     * @param token The token of the user making the request.
     * @return A response entity containing the list of role-permissions in the system.
     */
    @GetMapping(path = "/get-all")
    public ResponseEntity<Object> getRolePermissions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getRolePermissions(token));
    }

    /**
     * This endpoint is used to get the information of a role-permission.
     *
     * @param token The token of the user making the request.
     * @param id The id of the role-permission whose information is to be retrieved.
     * @return A response entity containing the information of the role-permission.
     */
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<Object> getRolePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        return ResponseEntity.ok(rolePermissionService.getRolePermission(token, id));
    }

    /**
     * This endpoint is used to delete a role-permission.
     *
     * @param token The token of the user making the request.
     * @param id The id of the role-permission to be deleted.
     * @return A response entity indicating that the role-permission has been deleted.
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Object> deleteRolePermission(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        rolePermissionService.deleteRolePermission(token, id);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to add a new role-permission to the system.
     *
     * @param token The token of the user making the request.
     * @param rolePermission The role-permission to be added to the system.
     * @return A response entity indicating that the role-permission has been added to the system.
     */
    @PostMapping(path = "/add")
    public ResponseEntity<Object> addRolePermission(@RequestHeader("Authorization") String token, @RequestBody RolePermissionDTO rolePermission) {
        rolePermissionService.addRolePermission(token, rolePermission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This endpoint is used to update the information of a role-permission.
     *
     * @param token The token of the user making the request.
     * @param rolePermission The updated information of the role-permission.
     * @return A response entity indicating that the information of the role-permission has been updated.
     */
    @PutMapping(path = "/update-role-permission")
    public ResponseEntity<Object> updateRolePermission(@RequestHeader("Authorization") String token, @RequestBody RolePermissionDTO rolePermission) {
        rolePermissionService.updateRolePermission(token, rolePermission);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to get the role-permissions of a role.
     *
     * @param token The token of the user making the request.
     * @param roleId The id of the role whose role-permissions are to be retrieved.
     * @return A response entity containing the role-permissions of the role.
     */
    @GetMapping(path = "/get-by-role/{roleId}")
    public ResponseEntity<Object> getRolePermissionsByRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        return ResponseEntity.ok(rolePermissionService.getPermissionsByRole(token, roleId));
    }

    /**
     * This endpoint is used to get the role-permissions of a permission.
     *
     * @param token The token of the user making the request.
     * @param permissionId The id of the permission whose role-permissions are to be retrieved.
     * @return A response entity containing the role-permissions of the permission.
     */
    @GetMapping(path = "/get-by-permission/{permissionId}")
    public ResponseEntity<Object> getRolePermissionsByPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        return ResponseEntity.ok(rolePermissionService.getRolesByPermission(token, permissionId));
    }

    /**
     * This endpoint is used to get all the roles in the system.
     *
     * @param token The token of the user making the request.
     * @return A response entity containing the list of roles in the system.
     */
    @GetMapping(path = "get-roles")
    public ResponseEntity<Object> getRoles(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getRoles(token));
    }

    /**
     * This endpoint is used to get all the permissions in the system.
     *
     * @param token The token of the user making the request.
     * @return A response entity containing the list of permissions in the system.
     */
    @GetMapping(path = "get-permissions")
    public ResponseEntity<Object> getPermissions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(rolePermissionService.getPermissions(token));
    }

    /**
     * This endpoint is used to get the information of a role.
     *
     * @param token The token of the user making the request.
     * @param roleId The id of the role whose information is to be retrieved.
     * @return A response entity containing the information of the role.
     */
    @GetMapping(path = "/get-role/{roleId}")
    public ResponseEntity<Object> getRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        return ResponseEntity.ok(rolePermissionService.getRole(token, roleId));
    }

    /**
     * This endpoint is used to get the information of a permission.
     *
     * @param token The token of the user making the request.
     * @param permissionId The id of the permission whose information is to be retrieved.
     * @return A response entity containing the information of the permission.
     */
    @GetMapping(path = "/get-permission/{permissionId}")
    public ResponseEntity<Object> getPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        return ResponseEntity.ok(rolePermissionService.getPermission(token, permissionId));
    }

    /**
     * This endpoint is used to delete a role.
     * It deletes the role and the role-permissions associated with the role.
     *
     * @param token The token of the user making the request.
     * @param roleId The id of the role to be deleted.
     * @return A response entity indicating that the role has been deleted.
     */
    @DeleteMapping(path = "/delete-role/{roleId}")
    public ResponseEntity<Object> deleteRolePermissionsByRole(@RequestHeader("Authorization") String token, @PathVariable Integer roleId) {
        rolePermissionService.deleteRole(token, roleId);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to delete a permission.
     * It deletes the permission and the role-permissions associated with the permission.
     *
     * @param token The token of the user making the request.
     * @param permissionId The id of the permission to be deleted.
     * @return A response entity indicating that the permission has been deleted.
     */
    @DeleteMapping(path = "/delete-permission/{permissionId}")
    public ResponseEntity<Object> deleteRolePermissionsByPermission(@RequestHeader("Authorization") String token, @PathVariable Integer permissionId) {
        rolePermissionService.deletePermission(token, permissionId);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to add a new role to the system.
     *
     * @param token The token of the user making the request.
     * @param role The role to be added to the system.
     * @return A response entity indicating that the role has been added to the system.
     */
    @PostMapping(path = "/add-role")
    public ResponseEntity<Object> addRole(@RequestHeader("Authorization") String token, @RequestBody RoleDTO role) {
        rolePermissionService.addRole(token, role);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This endpoint is used to add a new permission to the system.
     *
     * @param token The token of the user making the request.
     * @param permission The permission to be added to the system.
     * @return A response entity indicating that the permission has been added to the system.
     */
    @PostMapping(path = "/add-permission")
    public ResponseEntity<Object> addPermission(@RequestHeader("Authorization") String token, @RequestBody PermissionDTO permission) {
        rolePermissionService.addPermission(token, permission);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * This endpoint is used to update the information of a role.
     *
     * @param token The token of the user making the request.
     * @param role The updated information of the role.
     * @return A response entity indicating that the information of the role has been updated.
     */
    @PutMapping(path = "/update-role")
    public ResponseEntity<Object> updateRole(@RequestHeader("Authorization") String token, @RequestBody RoleDTO role) {
        rolePermissionService.updateRole(token, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to update the information of a permission.
     *
     * @param token The token of the user making the request.
     * @param permission The updated information of the permission.
     * @return A response entity indicating that the information of the permission has been updated.
     */
    @PutMapping(path = "/update-permission")
    public ResponseEntity<Object> updatePermission(@RequestHeader("Authorization") String token, @RequestBody PermissionDTO permission) {
        rolePermissionService.updatePermission(token, permission);
        return ResponseEntity.noContent().build();
    }
}