package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.model.RolePermission;

public class RolePermissionMapper {
    public static RolePermissionDTO toDTO(RolePermission rolePermission) {
        if (rolePermission == null) {
            return null;
        }

        return new RolePermissionDTO(
                rolePermission.getId(),
                rolePermission.getRole(),
                rolePermission.getPermission(),
                rolePermission.getDescription()
        );
    }

    public static RolePermission toEntity(RolePermissionDTO rolePermissionDTO) {
        if (rolePermissionDTO == null) {
            return null;
        }

        RolePermission rolePermission = new RolePermission(
                rolePermissionDTO.getRoleType(),
                rolePermissionDTO.getAccess(),
                rolePermissionDTO.getDetails()
        );

        if (rolePermissionDTO.getIdentifier() != null) {
            rolePermission.setId(rolePermissionDTO.getIdentifier());
        }

        return rolePermission;
    }
}
