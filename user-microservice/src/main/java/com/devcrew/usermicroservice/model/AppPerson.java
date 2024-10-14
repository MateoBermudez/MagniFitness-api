package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table (
        name = "APP_PERSON",
        schema = "dbo"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppPerson {

    @Id
    @SequenceGenerator(
            name = "app_person_sequence",
            sequenceName = "app_person_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_person_sequence"
    )
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "date_of_birth")
    private LocalDate date_of_birth;

    @Column(name = "personal_info")
    private String personalInfo;
    @Transient
    private Integer age;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    public AppPerson(String name, String last_name, LocalDate date_of_birth, AppUser appUser) {
        this.name = name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.appUser = appUser;
    }

}
