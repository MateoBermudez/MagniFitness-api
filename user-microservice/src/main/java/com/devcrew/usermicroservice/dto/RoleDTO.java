package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RoleDTO is a class used to create a data transfer object for the role entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    /**
     * The role_id field is used to store the identifier of the role.
     */
    private Integer role_id;
    /**
     * The role_name field is used to store the name of the role.
     */
    private String role_name;
}
