package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table (
        name = "APP_USER",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = "email"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    private Integer id;


    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String hashed_password;

//    Two-step verification later

    @Column(name = "authenticated")
    @NotNull
    private boolean authenticated = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    private AppPerson appPerson;

    public AppUser(String username, String email, String hashed_password, boolean authenticated, LocalDate createdAt, LocalDate updatedAt, AppPerson appPerson) {
        this.email = email;
        this.hashed_password = hashed_password;
        this.username = username;
        this.authenticated = authenticated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.appPerson = appPerson;
    }
}
