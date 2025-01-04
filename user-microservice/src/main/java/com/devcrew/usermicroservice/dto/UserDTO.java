package com.devcrew.usermicroservice.dto;

import com.devcrew.usermicroservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * UserDTO is a class used to create a data transfer object for the user entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    /**
     * The id field is used to store the identifier of the user.
     */
    private Integer id;
    /**
     * The user_name field is used to store the name of the user.
     */
    private String user_name;

    /**
     * The mail field is used to store the mail of the user.
     */
    private String mail;
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
    /**
     * The image field is used to store the image of the user. URL of the image. (URI)
     */
    private String image;
}
