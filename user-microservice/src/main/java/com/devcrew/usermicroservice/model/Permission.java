package com.devcrew.usermicroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    public Permission(String name) {
        this.name = name;
    }

}
