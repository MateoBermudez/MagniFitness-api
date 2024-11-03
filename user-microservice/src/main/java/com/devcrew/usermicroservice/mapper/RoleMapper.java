package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.model.Role;

/**
 * RoleMapper is a class that maps Role objects to RoleDTO objects and vice versa.
 */
public class RoleMapper {

    /**
     * Maps a Role object to a RoleDTO object.
     *
     * @param role the Role object to be mapped
     * @return the RoleDTO object
     */
    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    /**
     * Maps a RoleDTO object to a Role object.
     * If the id is null, it will be ignored.
     *
     * @param roleDTO the RoleDTO object to be mapped
     * @return the Role object
     */
    public static Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();

        if (roleDTO.getRole_id() != null) {
            role.setId(roleDTO.getRole_id());
        }

        role.setName(roleDTO.getRole_name());
        return role;
    }
}
