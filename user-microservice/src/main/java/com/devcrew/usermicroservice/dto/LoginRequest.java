package com.devcrew.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest is a class used to create a request object for the login process.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * The identifier field is used to store the identifier of the user.
     * It can be either the email or the username of the user.
     */
    private String identifier;
    /**
     * The password field is used to store the password of the user.
     */
    private String password;
}
