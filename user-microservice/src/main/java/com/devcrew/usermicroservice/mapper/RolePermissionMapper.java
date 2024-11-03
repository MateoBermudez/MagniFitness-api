package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.model.RolePermission;

/**
 * RolePermissionMapper is a class that maps RolePermission objects to RolePermissionDTO objects and vice versa.
 */
public class RolePermissionMapper {

    /**
     * Maps a RolePermission object to a RolePermissionDTO object.
     *
     * @param rolePermission the RolePermission object to be mapped
     * @return the RolePermissionDTO object
     */
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

    /**
     * Maps a RolePermissionDTO object to a RolePermission object.
     * If the id is null, it will be ignored.
     *
     * @param rolePermissionDTO the RolePermissionDTO object to be mapped
     * @return the RolePermission object
     */
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
