package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PermissionDTO is a class used to create a data transfer object for the permission entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    /**
     * The permission_id field is used to store the identifier of the permission.
     */
    private Integer permission_id;
    /**
     * The permission_name field is used to store the name of the permission.
     */
    private String permission_name;
}
