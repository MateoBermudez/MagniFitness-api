package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (
        name = "APP_USER",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = "email"),
                @UniqueConstraint(name = "password_unique", columnNames = "password")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
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

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private AppPerson appPerson;

    public AppUser(String username, String email, String hashed_password) {
        this.email = email;
        this.hashed_password = hashed_password;
        this.username = username;
    }
}
