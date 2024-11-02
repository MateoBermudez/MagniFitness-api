package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.AuthResponse;
import com.devcrew.usermicroservice.dto.LoginRequest;
import com.devcrew.usermicroservice.dto.RegisterRequest;
import com.devcrew.usermicroservice.exception.BadCredentialsException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.mapper.PersonMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.RoleRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing authentication operations.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * User repository that provides access to user data.
     */
    private final UserRepository userRepository;

    /**
     * JWT service that provides JWT token operations.
     */
    private final JwtService jwtService;

    /**
     * Password encoder that provides password encoding operations.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Authentication manager that provides authentication operations.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Role repository that provides access to role data.
     */
    private final RoleRepository roleRepository;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login request containing the user's credentials
     * @return an AuthResponse containing the JWT token
     */
    public AuthResponse login(LoginRequest request) {
        try {
            UserDetails userDetails;

            // Not an email -> Search by username
            if (!ValidationUtils.isLoginIdEmail(request.getIdentifier())) {
                userDetails = userRepository.findByUsername(request.getIdentifier())
                        .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            }
            // Email -> Search by email
            else {
                userDetails = userRepository.findByEmail(request.getIdentifier())
                        .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), request.getPassword()));

            String token = jwtService.getToken(userDetails);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        } catch (BadCredentialsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Registers a new user and generates a JWT token.
     *
     * @param request the register request containing the user's details
     * @return an AuthResponse containing the JWT token
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            ValidationUtils.isEmailValid(request.getMail());

            AppUser userValidation = userRepository.findByUsername(request.getUser_name()).orElse(null);

            if (userValidation != null) {
                throw new UserAlreadyExistsException("Username already exists");
            }

            userValidation = userRepository.findByEmail(request.getMail()).orElse(null);

            if (userValidation != null) {
                throw new UserAlreadyExistsException("Email already exists");
            }

            Role defaultRole = roleRepository.findByName("USER").orElse(null);

            AppUser user = AppUser.builder()
                    .username(request.getUser_name())
                    .email(request.getMail())
                    .hashed_password(passwordEncoder.encode(request.getPassword()))
                    .role(defaultRole)
                    .appPerson(PersonMapper.toEntity(request.getPerson()))
                    .build();

            user.getAppPerson().setAppUser(user);

            userRepository.save(user);

            return AuthResponse.builder()
                    .token(jwtService.getToken(user))
                    .build();
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}