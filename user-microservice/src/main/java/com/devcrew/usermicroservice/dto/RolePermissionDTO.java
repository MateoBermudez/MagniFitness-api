package com.devcrew.usermicroservice.dto;

import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDTO {
    private Integer identifier;
    private Role roleType;
    private Permission access;
    private String details;
}
