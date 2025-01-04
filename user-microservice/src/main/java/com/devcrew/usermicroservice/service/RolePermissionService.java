package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.CustomException;
import com.devcrew.usermicroservice.mapper.PermissionMapper;
import com.devcrew.usermicroservice.mapper.RoleMapper;
import com.devcrew.usermicroservice.mapper.RolePermissionMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.model.RolePermission;
import com.devcrew.usermicroservice.repository.PermissionRepository;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.RoleRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.AuthorizationUtils;
import com.devcrew.usermicroservice.utils.JsonBuilderUtils;
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing a role and permission-related operations.
 */
@Service
public class RolePermissionService {

    /**
     * LogSenderService object for sending logs to the log microservice.
     */
    private final LogSenderService logSenderService;

    /**
     * RolePermissionRepository object for accessing the role_permission table in the database.
     */
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * UserRepository object for accessing the user table in the database.
     */
    private final UserRepository userRepository;

    /**
     * JwtValidation object for validating the JWT token.
     */
    private final JwtValidation jwtValidation;

    /**
     * RoleRepository object for accessing the role table in the database.
     */
    private final RoleRepository roleRepository;

    /**
     * PermissionRepository object for accessing the permission table in the database.
     */
    private final PermissionRepository permissionRepository;

    /**
     * Constructor for RolePermissionService,
     * which initializes the repositories and utilities using the @Autowired annotation.
     *
     * @param rolePermissionRepository the role permission repository
     * @param userRepository the user repository
     * @param jwtValidation the JWT validation utility
     * @param roleRepository the role repository
     * @param permissionRepository the permission repository
     */
    @Autowired
    public RolePermissionService(RolePermissionRepository rolePermissionRepository, UserRepository userRepository, JwtValidation jwtValidation,
                                 RoleRepository roleRepository, PermissionRepository permissionRepository, LogSenderService logSenderService) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.jwtValidation = jwtValidation;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.logSenderService = logSenderService;
    }

    /**
     * Retrieves all role permissions.
     *
     * @param token the JWT token of the user requesting the role permissions
     * @return a list of RolePermissionDTO
     */
    public List<RolePermissionDTO> getRolePermissions(String token) {
        AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
        return rolePermissionRepository.findAll().stream().map(RolePermissionMapper::toDTO).toList();
    }

    /**
     * Deletes a role permission.
     *
     * @param token the JWT token of the user requesting the deletion
     * @param id the ID of the role permission to be deleted in the database
     */
    @Transactional
    public void deleteRolePermission(String token, Integer id) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            RolePermission rolePermission = rolePermissionRepository.findById(id).orElseThrow(
                    () -> new BadRequestException("RolePermission not found")
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "Permission", "role_permission", user.getId(),
                    "User with " + username + " username has deleted a role permission.",
                    JsonBuilderUtils.jsonBuilder(rolePermission),
                    "{}"
            );

            rolePermissionRepository.deleteById(id);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Adds a new role permission.
     *
     * @param token the JWT token of the user adding the role permission
     * @param rolePermission the role permission DTO to be added to the database
     */
    @Transactional
    public void addRolePermission(String token, RolePermissionDTO rolePermission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            RolePermission rolePermissionEntity = RolePermissionMapper.toEntity(rolePermission);

            logSenderService.sendLog(
                    null, null, null,
                    "Create", "Permission", "role_permission", user.getId(),
                    "User with " + username + " username has added a new role permission.",
                    "{}",
                    JsonBuilderUtils.jsonBuilder(rolePermissionEntity)
            );

            rolePermissionRepository.save(rolePermissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error adding RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Updates an existing role permission.
     *
     * @param token the JWT token of the user updating the role permission
     * @param rolePermission the role permission DTO to be updated in the database
     */
    @Transactional
    public void updateRolePermission(String token, RolePermissionDTO rolePermission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            RolePermission rolePermissionEntity = RolePermissionMapper.toEntity(rolePermission);
            RolePermission roleToUpdate = rolePermissionRepository.findById(rolePermissionEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            if (roleToUpdate.equals(rolePermissionEntity)) {
                throw new BadRequestException("Data is the same");
            }

            logSenderService.sendLog(
                    null, null, null,
                    "Update", "Permission", "role_permission", user.getId(),
                    "User with " + username + " username has updated a role permission.",
                    JsonBuilderUtils.jsonBuilder(roleToUpdate),
                    JsonBuilderUtils.jsonBuilder(rolePermissionEntity)
            );

            rolePermissionRepository.save(rolePermissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves a role permission by its ID.
     *
     * @param token the JWT token of the user requesting the role permission
     * @param id the ID of the role permission to be retrieved
     * @return the RolePermissionDTO object with the RolePermission data
     */
    public RolePermissionDTO getRolePermission(String token, Integer id) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            RolePermission rolePermission = rolePermissionRepository.findById(id).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            return RolePermissionMapper.toDTO(rolePermission);
        } catch (CustomException ex) {
            throw new CustomException("Error getting RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves permissions by role.
     *
     * @param token the JWT token of the user requesting the permissions
     * @param roleId the ID of the role to retrieve permissions for
     * @return a list of PermissionDTO
     */
    public List<PermissionDTO> getPermissionsByRole(String token, Integer roleId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            List<Permission> permissions = rolePermissionRepository.findByRole(roleId);
            return permissions.stream().map(PermissionMapper::toDTO).toList();
        } catch (CustomException ex) {
            throw new CustomException("Error getting permissions by role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves roles by permission.
     *
     * @param token the JWT token of the user requesting the roles
     * @param permissionId the ID of the permission to retrieve roles for
     * @return a list of RoleDTO with the roles that have the specified permission
     */
    public List<RoleDTO> getRolesByPermission(String token, Integer permissionId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            List<Role> roles = rolePermissionRepository.findByPermission(permissionId);
            return roles.stream().map(RoleMapper::toDTO).toList();
        } catch (CustomException ex) {
            throw new CustomException("Error getting roles by permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves all roles.
     *
     * @param token the JWT token of the user requesting the roles
     * @return a list of RoleDTO with all roles
     */
    public List<RoleDTO> getRoles(String token) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            return roleRepository.findAll().stream().map(RoleMapper::toDTO).toList();
        } catch (CustomException ex) {
            throw new CustomException("Error getting roles\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves all permissions.
     *
     * @param token the JWT token of the user requesting the permissions
     * @return a list of PermissionDTO with all permissions
     */
    public List<PermissionDTO> getPermissions(String token) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            return permissionRepository.findAll().stream().map(PermissionMapper::toDTO).toList();
        } catch (CustomException ex) {
            throw new CustomException("Error getting permissions\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Deletes a role.
     *
     * @param token the JWT token of the user requesting the deletion
     * @param roleId the ID of the role to be deleted in the database
     */
    @Transactional
    public void deleteRole(String token, Integer roleId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Role role = roleRepository.findById(roleId).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "Role", "role", user.getId(),
                    "User with " + username + " username has deleted a role.",
                    JsonBuilderUtils.jsonBuilder(role),
                    "{}"
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "Role", "role_permission", user.getId(),
                    "User with " + username + " username has deleted the connections between the role and the permission.",
                    JsonBuilderUtils.jsonBuilder(role),
                    "{}"
            );

            rolePermissionRepository.deleteByRole(role.getId());
            roleRepository.delete(role);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Deletes a permission.
     *
     * @param token the JWT token of the user requesting the deletion
     * @param permissionId the ID of the permission to be deleted in the database
     */
    @Transactional
    public void deletePermission(String token, Integer permissionId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                    () -> new BadRequestException("Permission not found")
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "Permission", "permission", user.getId(),
                    "User with " + username + " username has deleted a permission.",
                    JsonBuilderUtils.jsonBuilder(permission),
                    "{}"
            );

            logSenderService.sendLog(
                    null, null, null,
                    "Delete", "Permission", "role_permission", user.getId(),
                    "User with " + username + " username has deleted the connections between the role and the permission.",
                    JsonBuilderUtils.jsonBuilder(permission),
                    "{}"
            );

            rolePermissionRepository.deleteByPermission(permission.getId());
            permissionRepository.delete(permission);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Adds a new role.
     *
     * @param token the JWT token of the user adding the role
     * @param role the role DTO to be added to the database
     */
    @Transactional
    public void addRole(String token, RoleDTO role) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Role roleToAdd = RoleMapper.toEntity(role);

            logSenderService.sendLog(
                    null, null, null,
                    "Create", "Role", "role", user.getId(),
                    "User with " + username + " username has added a new role.",
                    "{}",
                    JsonBuilderUtils.jsonBuilder(roleToAdd)
            );

            roleRepository.save(roleToAdd);
        } catch (CustomException ex) {
            throw new CustomException("Error adding role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Adds a new permission.
     *
     * @param token the JWT token of the user adding the permission
     * @param permission the permission DTO to be added to the database
     */
    @Transactional
    public void addPermission(String token, PermissionDTO permission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Permission permissionToAdd = PermissionMapper.toEntity(permission);

            logSenderService.sendLog(
                    null, null, null,
                    "Create", "Permission", "permission", user.getId(),
                    "User with " + username + " username has added a new permission.",
                    "{}",
                    JsonBuilderUtils.jsonBuilder(permissionToAdd)
            );

            permissionRepository.save(permissionToAdd);
        } catch (CustomException ex) {
            throw new CustomException("Error adding permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    //Review this method; it has the potential to throw an exception with the cascade not propagating to RolePermission
    /**
     * Updates a role.
     *
     * @param token the JWT token of the user updating the role
     * @param role the role DTO to be updated in the database
     */
    @Transactional
    public void updateRole(String token, RoleDTO role) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Role roleEntity = RoleMapper.toEntity(role);
            Role roleToUpdate = roleRepository.findById(roleEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            if (roleToUpdate.equals(roleEntity)) {
                throw new BadRequestException("Data is the same");
            }
            //Cascade should propagate to RolePermission

            logSenderService.sendLog(
                    null, null, null,
                    "Update", "Role", "role", user.getId(),
                    "User with " + username + " username has updated a role.",
                    JsonBuilderUtils.jsonBuilder(roleToUpdate),
                    JsonBuilderUtils.jsonBuilder(roleEntity)
            );

            roleRepository.save(roleEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    //Review this method; it has the potential to throw an exception with the cascade not propagating to RolePermission
    /**
     * Updates a permission.
     *
     * @param token the JWT token of the user updating the permission
     * @param permission the permission DTO to be updated in the database
     */
    @Transactional
    public void updatePermission(String token, PermissionDTO permission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);

            String username = jwtValidation.validateUsernameFromToken(token);

            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new BadRequestException("User not found")
            );

            Permission permissionEntity = PermissionMapper.toEntity(permission);
            Permission permissionToUpdate = permissionRepository.findById(permissionEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Permission not found")
            );
            if (permissionToUpdate.equals(permissionEntity)) {
                throw new BadRequestException("Data is the same");
            }
            //Cascade should propagate to RolePermission

            logSenderService.sendLog(
                    null, null, null,
                    "Update", "Permission", "permission", user.getId(),
                    "User with " + username + " username has updated a permission.",
                    JsonBuilderUtils.jsonBuilder(permissionToUpdate),
                    JsonBuilderUtils.jsonBuilder(permissionEntity)
            );

            permissionRepository.save(permissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves a role by its ID.
     *
     * @param token the JWT token of the user requesting the role
     * @param roleId the ID of the role to be retrieved
     * @return the role DTO object with the role data
     */
    public RoleDTO getRole(String token, Integer roleId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Role role = roleRepository.findById(roleId).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            return RoleMapper.toDTO(role);
        } catch (CustomException ex) {
            throw new CustomException("Error getting role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Retrieves a permission by its ID.
     *
     * @param token the JWT token of the user requesting the permission
     * @param permissionId the ID of the permission to be retrieved
     * @return the permission DTO object with the permission data
     */
    public PermissionDTO getPermission(String token, Integer permissionId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                    () -> new BadRequestException("Permission not found")
            );
            return PermissionMapper.toDTO(permission);
        } catch (CustomException ex) {
            throw new CustomException("Error getting permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}