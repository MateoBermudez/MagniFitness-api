package com.devcrew.usermicroservice.dto;

import com.devcrew.usermicroservice.model.Permission;
import com.devcrew.usermicroservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RolePermissionDTO is a class used to create a data transfer object for the role_permission entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDTO {
    /**
     * The identifier field is used to store the identifier of the role_permission.
     */
    private Integer identifier;
    /**
     * The roleType field is used to store the role of the role_permission.
     */
    private Role roleType;
    /**
     * The access field is used to store the permission of the role_permission.
     */
    private Permission access;
    /**
     * The details field is used to store the details of the role_permission.
     * It can be used to store additional information about the role_permission.
     */
    private String details;
}
