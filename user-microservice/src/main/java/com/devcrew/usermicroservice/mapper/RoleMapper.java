package com.devcrew.usermicroservice.mapper;

import com.devcrew.usermicroservice.dto.RoleDTO;
import com.devcrew.usermicroservice.model.Role;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    public static Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();

        if (roleDTO.getRole_id() != null) {
            role.setId(roleDTO.getRole_id());
        }

        role.setName(roleDTO.getRole_name());
        return role;
    }
}
