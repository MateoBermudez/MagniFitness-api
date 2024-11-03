package com.devcrew.usermicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * RolePermission entity, relation between Role and Permission entities,
 * with a description of the relation.
 * It represents the permissions that a role has.
 * A role can have multiple permissions.
 * A permission can be assigned to multiple roles.
 */
@Entity
@Table(name = "ROLE_PERMISSION",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "role_permission_unique", columnNames = {"role", "permission"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    /**
     * RolePermission id, auto-generated, unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Role entity, many-to-one relation.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    /**
     * Permission entity, many-to-one relation.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

    /**
     * Description of the relation between Role and Permission.
     */
    @NotNull
    @Column(name = "description")
    private String description;

    /**
     * RolePermission constructor.
     *
     * @param role        Role entity.
     * @param permission  Permission entity.
     * @param description Description of the relation between Role and Permission.
     */
    public RolePermission(Role role, Permission permission, String description) {
        this.role = role;
        this.permission = permission;
        this.description = description;
    }
}