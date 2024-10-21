package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.AuthResponse;
import com.devcrew.usermicroservice.dto.LoginRequest;
import com.devcrew.usermicroservice.dto.RegisterRequest;
import com.devcrew.usermicroservice.exception.BadCredentialsException;
import com.devcrew.usermicroservice.mapper.PersonMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        UserDetails userDetails;

        // Not an email -> Search by username
        if (!ValidationUtils.isEmailValid(request.getIdentifier())) {
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
    }

    public AuthResponse register(RegisterRequest request) {

        if (!ValidationUtils.isEmailValid(request.getMail())) {
            throw new BadCredentialsException("Invalid email");
        }

        AppUser user = AppUser.builder()
                .username(request.getUser_name())
                .email(request.getMail())
                .hashed_password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .appPerson(PersonMapper.toEntity(request.getPerson()))
                .build();

        user.getAppPerson().setAppUser(user);

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
