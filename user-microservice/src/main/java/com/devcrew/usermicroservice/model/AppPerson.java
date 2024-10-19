package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

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

    @Id
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
    @JsonBackReference
    @ToString.Exclude
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    public AppPerson(String name, String last_name, LocalDate date_of_birth, String personalInfo, Integer age, AppUser appUser) {
        this.name = name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.personalInfo = personalInfo;
        this.age = age;
        this.appUser = appUser;
    }

    public Integer getAge() {
        return LocalDate.now().getYear() - date_of_birth.getYear();
    }

}
