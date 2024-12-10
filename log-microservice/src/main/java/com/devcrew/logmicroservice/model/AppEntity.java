package com.devcrew.logmicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
        name = "APP_ENTITY",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class AppEntity {

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

    @Column(name = "name")
    @NotNull
    private String name;
}
