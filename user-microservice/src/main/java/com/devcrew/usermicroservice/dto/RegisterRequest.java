package com.devcrew.usermicroservice.dto;

import com.devcrew.usermicroservice.model.Role;
import lombok.*;

import java.time.LocalDate;

/**
 * RegisterRequest is a class used to create a request object for the registration process.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    /**
     * The user_name field is used to store the username of the user.
     */
    private String user_name;
    /**
     * The mail field is used to store the email of the user.
     */
    private String mail;
    /**
     * The password field is used to store the plain password of the user.
     */
    private String password;
    /**
     * The authenticated field is used to store the authentication status of the user.
     */
    private boolean authenticated;
    /**
     * The user_created_at field is used to store the creation date of the user.
     */
    private LocalDate user_created_at;
    /**
     * The user_updated_at field is used to store the last update date of the user.
     */
    private LocalDate user_updated_at;
    /**
     * The role field is used to store the role object of the user. (Role related to the user)
     */
    private Role role;
    /**
     * The person field is used to store the person object of the user. (Person related to the user)
     */
    @ToString.Exclude
    private PersonDTO person;
}
