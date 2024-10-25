package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.BadCredentialsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtValidation {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    JwtValidation(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

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

    public String validateRoleFromToken(String token) {
        String tokenFin = token.substring(7); //Remove Bearer from token
        Claims claims = jwtService.getAllClaimsFromToken(tokenFin);
        return claims.get("role", String.class);
    }
}
