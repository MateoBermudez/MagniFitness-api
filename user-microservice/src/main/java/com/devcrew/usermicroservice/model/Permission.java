package com.devcrew.usermicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Permission entity represents a permission that a user can have.
 */
@Entity
@Table(name = "PERMISSION",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "permission_unique", columnNames = {"name"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * The unique identifier of the permission.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the permission.
     */
    @Column(name = "name")
    @NotNull
    private String name;

    /**
     * Constructor with the name of the permission.
     *
     * @param name the name of the permission
     */
    public Permission(String name) {
        this.name = name;
    }

}
