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

/**
 * Entity class representing a user in the application.
 */
@Entity
@Table (
        name = "APP_USER",
        schema = "dbo",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = "email"),
                @UniqueConstraint(name = "username_unique", columnNames = "username")
        },
        indexes= {
            @Index(name = "idx_username", columnList = "username"),
            @Index(name = "idx_email", columnList = "email")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class AppUser implements UserDetails {

    /**
     * The unique identifier of the user.
     */
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


    /**
     * The username of the user.
     */
    @Column(name = "username")
    @NotNull
    private String username;

    /**
     * The email of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * The password of the user.
     */
    @Column(name = "password")
    @JsonIgnore
    private String hashed_password;

    /**
     * The authentication status of the user.
     */
    @Default
    @Column(name = "authenticated")
    @NotNull
    private boolean authenticated = false;

    /**
     * The date the user was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    /**
     * The date the user was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    /**
     * The role of the user.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonManagedReference
    @ToString.Exclude
    Role role;

    /**
     * The enabled status of the user.
     */
    @Default
    @Column(name = "enabled")
    Boolean enabled = true;

    /**
     * The logged in status of the user.
     */
    @Column(name = "logged_in")
    @NotNull
    Boolean loggedIn = false;

    /**
     * The person associated with the user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    private AppPerson appPerson;

    /**
     * The image URI of the user. (URL of the image)
     */
    @Column(name = "image_uri")
    @JsonIgnore
    String imageUri;

    /**
     * The two-factor authentication secret key of the user.
     */
    @Column(name = "2fa_secret_key")
    @JsonIgnore
    String twoFactorAuthSecretKey;

    /**
     * Constructor with parameters.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param authenticated the authentication status of the user
     * @param createdAt the date the user was created
     * @param updatedAt the date the user was last updated
     * @param appPerson the associated AppPerson entity
     * @param role the role of the user
     * @param imageUri the image URI of the user
     */

    @JsonIgnore
    public AppUser(String username, String email, boolean authenticated, LocalDate createdAt, LocalDate updatedAt, AppPerson appPerson, Role role, String imageUri) {
        this.email = email;
        this.username = username;
        this.authenticated = authenticated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.appPerson = appPerson;
        this.role = role;
        this.imageUri = imageUri;
    }

    /**
     * Overridden method of the UserDetails interface.
     * Collection of authorities of the user.
     * @return the role of the user
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    /**
     * Overridden method of the UserDetails interface.
     * Getter for the password of the user.
     * @return the password of the user
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return hashed_password;
    }

    /**
     * Overridden method of the UserDetails interface.
     * isAccountNonExpired method.
     * @return true for all users
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Overridden method of the UserDetails interface.
     * isAccountNonLocked method.
     * @return true for all users
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Overridden method of the UserDetails interface.
     * isCredentialsNonExpired method.
     * @return true for all users
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Overridden method of the UserDetails interface.
     * isEnabled method.
     * @return the enabled status of the user
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Method to check if the user has two-factor authentication enabled.
     * @return true if the user has two-factor authentication enabled
     */
    @JsonIgnore
    public boolean hasTwoFactorAuth() {
        return twoFactorAuthSecretKey != null && authenticated;
    }
}
