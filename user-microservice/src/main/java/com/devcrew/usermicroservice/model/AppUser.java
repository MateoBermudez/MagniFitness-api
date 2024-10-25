package com.devcrew.usermicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Builder.Default;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table (
        name = "APP_USER",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = "email"),
                @UniqueConstraint(name = "username_unique", columnNames = "username")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class AppUser implements UserDetails {

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
    @NotNull
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
    private boolean authenticated;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    Role role;

    @Default
    @Column(name = "enabled")
    Boolean enabled = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    private AppPerson appPerson;

    public AppUser(String username, String email, boolean authenticated, LocalDate createdAt, LocalDate updatedAt, AppPerson appPerson, Role role) {
        this.email = email;
        this.username = username;
        this.authenticated = authenticated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.appPerson = appPerson;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return hashed_password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
