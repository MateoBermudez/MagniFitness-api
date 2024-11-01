package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.CustomException;
import com.devcrew.usermicroservice.mapper.PermissionMapper;
import com.devcrew.usermicroservice.mapper.RoleMapper;
import com.devcrew.usermicroservice.mapper.RolePermissionMapper;
import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.model.RolePermission;
import com.devcrew.usermicroservice.repository.PermissionRepository;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.RoleRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.AuthorizationUtils;
import com.devcrew.usermicroservice.utils.JwtValidation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final JwtValidation jwtValidation;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public RolePermissionService(RolePermissionRepository rolePermissionRepository, UserRepository userRepository, JwtValidation jwtValidation,
                                 RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.jwtValidation = jwtValidation;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<RolePermissionDTO> getRolePermissions(String token) {
        AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
        return rolePermissionRepository.findAll().stream().map(RolePermissionMapper::toDTO).toList();
    }

    @Transactional
    public void deleteRolePermission(String token, Integer id) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            rolePermissionRepository.deleteById(id);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void addRolePermission(String token, RolePermissionDTO rolePermission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            RolePermission rolePermissionEntity = RolePermissionMapper.toEntity(rolePermission);
            rolePermissionRepository.save(rolePermissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error adding RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void updateRolePermission(String token, RolePermissionDTO rolePermission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            RolePermission rolePermissionEntity = RolePermissionMapper.toEntity(rolePermission);
            RolePermission roleToUpdate = rolePermissionRepository.findById(rolePermissionEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            if (roleToUpdate.equals(rolePermissionEntity)) {
                throw new BadRequestException("Data is the same");
            }
            rolePermissionRepository.save(rolePermissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating RolePermission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

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

    @Transactional
    public void deleteRole(String token, Integer roleId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Role role = roleRepository.findById(roleId).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            rolePermissionRepository.deleteByRole(role.getId());
            roleRepository.delete(role);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void deletePermission(String token, Integer permissionId) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                    () -> new BadRequestException("Permission not found")
            );
            rolePermissionRepository.deleteByPermission(permission.getId());
            permissionRepository.delete(permission);
        } catch (CustomException ex) {
            throw new CustomException("Error deleting permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void addRole(String token, RoleDTO role) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Role roleToAdd = RoleMapper.toEntity(role);
            roleRepository.save(roleToAdd);
        } catch (CustomException ex) {
            throw new CustomException("Error adding role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void addPermission(String token, PermissionDTO permission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Permission permissionToAdd = PermissionMapper.toEntity(permission);
            permissionRepository.save(permissionToAdd);
        } catch (CustomException ex) {
            throw new CustomException("Error adding permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void updateRole(String token, RoleDTO role) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Role roleEntity = RoleMapper.toEntity(role);
            Role roleToUpdate = roleRepository.findById(roleEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            if (roleToUpdate.equals(roleEntity)) {
                throw new BadRequestException("Data is the same");
            }
            roleRepository.save(roleEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating role\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void updatePermission(String token, PermissionDTO permission) {
        try {
            AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
            Permission permissionEntity = PermissionMapper.toEntity(permission);
            Permission permissionToUpdate = permissionRepository.findById(permissionEntity.getId()).orElseThrow(
                    () -> new BadRequestException("Permission not found")
            );
            if (permissionToUpdate.equals(permissionEntity)) {
                throw new BadRequestException("Data is the same");
            }
            permissionRepository.save(permissionEntity);
        } catch (CustomException ex) {
            throw new CustomException("Error updating permission\n" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

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