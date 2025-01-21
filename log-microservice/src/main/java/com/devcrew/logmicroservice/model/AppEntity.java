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
 * App entity, represents all the tables of the project database.
 */
@Entity
@Table(
        name = "APP_ENTITY",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * AppEntity id
     */
    @Id
    @SequenceGenerator(
            name = "app_entity_sequence",
            sequenceName = "app_entity_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_entity_sequence"
    )
    private Integer id;

    /**
     * AppEntity name
     */
    @Column(name = "name")
    @NotNull
    private String name;
}
