package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for managing JWT operations.
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign the JWT.
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Expiration date of the JWT.
     */
    @Value("${jwt.expiration}")
    private long EXPIRATION_DATE;

    /**
     * User repository to access the database and retrieve the user's information.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for JwtService, initializes the user repository.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Generates a JWT token for a given user.
     * It puts the user's role as a claim in the token.
     *
     * @param user the user details to generate the token for
     * @return the generated JWT token for the user
     */
    public String getToken(UserDetails user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", getRoleFromUserDetails(user));
            return getToken(claims, user);
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    /**
     * Generates a JWT token with additional claims.
     * It sets the subject of the token to the user's username.
     * It sets the issued at date to the current date.
     * It sets the expiration date to the current date plus the expiration date.
     * It signs the token with the secret key.
     * It includes the additional claims in the token.
     * It returns the generated JWT token.
     *
     * @param extraClaims additional claims to be included in the token
     * @param user the user details
     * @return the generated JWT token
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        try {
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                    .signWith(SignatureAlgorithm.HS256, getKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token with extra claims", e);
        }
    }

    /**
     * Generates the signing key for the JWT token.
     *
     * @return the signing key
     */
    private Key getKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    /**
     * Retrieves the username from a JWT token.
     *
     * @param token the JWT token to retrieve the username from
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving username from token", e);
        }
    }

    /**
     * Validates if a JWT token is valid for a given user.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate the token against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            throw new RuntimeException("Error validating token", e);
        }
    }

    /**
     * Retrieves all claims from a JWT token.
     *
     * @param token the JWT token to retrieve the claims from
     * @return the claims from the token
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all claims from token", e);
        }
    }

    /**
     * Retrieves a specific claim from a JWT token.
     *
     * @param token the JWT token to retrieve the claim from
     * @param claimsResolver the function to resolve the claim
     * @param <T> the type of the claim to retrieve
     * @return the claim from the token
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving claim from token", e);
        }
    }

    /**
     * Retrieves the expiration date from a JWT token.
     *
     * @param token the JWT token to retrieve the expiration date from
     * @return the expiration date of the token
     */
    private Date getExpirationDateFromToken(String token) {
        try {
            return getClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving expiration date from token", e);
        }
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token to check if it is expired
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        try {
            return getExpirationDateFromToken(token).before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if token is expired", e);
        }
    }

    /**
     * Retrieves the role from the user details of a user.
     *
     * @param userDetails the user details to retrieve the role from
     * @return the role of the user
     */
    private String getRoleFromUserDetails(UserDetails userDetails) {
        try {
            AppUser user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                    () -> new UserDoesNotExistException("User not found")
            );
            return String.valueOf(user.getRole().getName());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving role from user details", e);
        }
    }
}