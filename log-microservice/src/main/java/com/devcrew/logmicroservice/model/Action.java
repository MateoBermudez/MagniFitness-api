package com.devcrew.logmicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Action entity, represents the action that the user has performed based on CRUD operations.
 */
@Entity
@Table (
        name = "ACTION",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Action id
     */
    @Id
    @SequenceGenerator(
            name = "action_sequence",
            sequenceName = "action_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "action_sequence"
    )
    private Integer id;

    /**
     * Action name
     */
    @Column(name = "name")
    @NotNull
    private String name;
}
