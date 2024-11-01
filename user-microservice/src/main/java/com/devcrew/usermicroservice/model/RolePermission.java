package com.devcrew.usermicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

    @NotNull
    @Column(name = "description")
    private String description;

    public RolePermission(Role role, Permission permission, String description) {
        this.role = role;
        this.permission = permission;
        this.description = description;
    }
}