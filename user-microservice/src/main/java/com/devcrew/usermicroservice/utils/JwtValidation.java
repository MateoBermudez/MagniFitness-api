package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.BadCredentialsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/**
 * JwtValidation class is used to validate the token and extract the username and role from the token.
 * It uses JwtService to validate the token and extract the username and role from the token.
 * It uses UserRepository to get the user details from the database.
 * {@code @Component} is used to indicate that the class is a Spring component.
 */
@Component
public class JwtValidation {

    /**
     * JwtService object is used to validate the token and extract the username and role from the token.
     */
    private final JwtService jwtService;

    /**
     * UserRepository object is used to get the user details from the database.
     */
    private final UserRepository userRepository;

    /**
     * JwtValidation constructor is used to initialize the JwtService and UserRepository objects,
     * using dependency injection.
     * @param jwtService JwtService object is used to validate the token and extract the username and role from the token.
     * @param userRepository UserRepository object is used to get the user details from the database.
     */
    @Autowired
    JwtValidation(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * validateUsernameFromToken method is used to validate the token and extract the username from the token.
     * It uses JwtService to validate the token and extract the username from the token.
     * It uses UserRepository to get the user details from the database.
     * It throws UserDoesNotExistException if the user does not exist.
     * It throws BadCredentialsException if the token is invalid.
     * @param token The token of the user.
     * @return String username is the username extracted from the token.
     */
    public String validateUsernameFromToken(String token) {
        String jwtToken = token.substring(7); //Remove Bearer from token
        String username = jwtService.getUsernameFromToken(jwtToken);
        UserDetails userDetails = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        if (!jwtService.isTokenValid(jwtToken, userDetails)) {
            throw new BadCredentialsException("Invalid token");
        }
        return username;
    }

    /**
     * validateRoleFromToken method is used to validate the token and extract the role from the token.
     * It uses JwtService to validate the token and extract the role from the token.
     * @param token The token of the user.
     * @return String role is the role extracted from the token.
     */
    public String validateRoleFromToken(String token) {
        String tokenFin = token.substring(7); //Remove Bearer from token
        Claims claims = jwtService.getAllClaimsFromToken(tokenFin);
        return claims.get("role", String.class);
    }
}
