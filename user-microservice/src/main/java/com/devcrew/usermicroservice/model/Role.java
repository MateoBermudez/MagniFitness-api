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
 * Role entity represents a role that a user can have.
 */
@Entity
@Table(name = "ROLE",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "role_unique", columnNames = {"name"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier of the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the role.
     */
    @Column(name = "name")
    @NotNull
    private String name;

    /**
     * Constructor with the name of the role.
     *
     * @param name the name of the role
     */
    public Role(String name) {
        this.name = name;
    }
}
