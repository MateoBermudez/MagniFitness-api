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
 * Module entity that represents the module of the application, it can be payment, user, etc.
 */
@Entity
@Table(
        name = "MODULE",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppModule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Module id
     */
    @Id
    @SequenceGenerator(
            name = "module_sequence",
            sequenceName = "module_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "module_sequence"
    )
    private Integer id;

    /**
     * Module name
     */
    @Column(name = "name")
    @NotNull
    private String name;
}
