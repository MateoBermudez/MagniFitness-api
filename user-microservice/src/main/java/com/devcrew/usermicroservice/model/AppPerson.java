package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;


/**
 * Entity class representing a person in the application.
 */
@Entity
@Table (
        name = "APP_PERSON",
        schema = "dbo"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class AppPerson {

    /**
     * The unique identifier of the person.
     */
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

    /**
     * The name of the person.
     */
    @Column(name = "name")
    @NotNull
    private String name;

    /**
     * The last name of the person.
     */
    @Column(name = "last_name")
    private String last_name;

    /**
     * The date of birth of the person.
     */
    @Column(name = "date_of_birth")
    private LocalDate date_of_birth;

    /**
     * The personal information of the person.
     */
    @Column(name = "personal_info")
    private String personalInfo;

    /**
     * The age of the person.
     */
    @Transient
    private Integer age;

    /**
     * The user associated with the person.
     */
    @OneToOne(mappedBy = "appPerson", cascade = CascadeType.ALL)
    @JsonBackReference
    @JsonIgnore
    @ToString.Exclude
    private AppUser appUser;

    /**
     * Constructor with parameters.
     *
     * @param name the name of the person
     * @param last_name the last name of the person
     * @param date_of_birth the date of birth of the person
     * @param personalInfo additional personal information
     * @param age the age of the person
     * @param appUser the associated AppUser entity
     */
    public AppPerson(String name, String last_name, LocalDate date_of_birth, String personalInfo, Integer age, AppUser appUser) {
        this.name = name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.personalInfo = personalInfo;
        this.age = age;
        this.appUser = appUser;
    }

    /**
     * Returns the age of the person.
     *
     * @return the age of the person
     */
    public Integer getAge() {
        return LocalDate.now().getYear() - date_of_birth.getYear();
    }

}
