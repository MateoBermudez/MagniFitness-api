package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.PermissionDTO;
import com.devcrew.usermicroservice.model.Permission;

/**
 * PermissionMapper is a class that maps Permission objects to PermissionDTO objects and vice versa.
 */
public class PermissionMapper {

    /**
     * Maps a Permission object to a PermissionDTO object.
     *
     * @param permission the Permission object to be mapped
     * @return the PermissionDTO object
     */
    public static PermissionDTO toDTO(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getName());
    }

    /**
     * Maps a PermissionDTO object to a Permission object.
     * If the id is null, it will be ignored.
     *
     * @param permissionDTO the PermissionDTO object to be mapped
     * @return the Permission object
     */
    public static Permission toEntity(PermissionDTO permissionDTO) {
        Permission permission = new Permission();

        if (permissionDTO.getPermission_id() != null) {
            permission.setId(permissionDTO.getPermission_id());
        }

        permission.setName(permissionDTO.getPermission_name());
        return permission;
    }
}
