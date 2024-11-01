package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.model.Permission;

public class PermissionMapper {

    public static PermissionDTO toDTO(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getName());
    }

    public static Permission toEntity(PermissionDTO permissionDTO) {
        Permission permission = new Permission();

        if (permissionDTO.getPermission_id() != null) {
            permission.setId(permissionDTO.getPermission_id());
        }

        permission.setName(permissionDTO.getPermission_name());
        return permission;
    }
}
