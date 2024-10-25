package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.exception.ForbiddenException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.UserMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.JwtValidation;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtValidation jwtValidation;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtValidation jwtValidation) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtValidation = jwtValidation;
    }

    public List<UserDTO> getUsers(String token) {
        ValidateAdmin(token);
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    public UserDTO getUserInfo(String token, String username) {
        AppUser user = ValidateAuthorizationForAdminAndUser(username, token);
        return UserMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(String username, String token) {
        AppUser user = ValidateAuthorizationForAdminAndUser(username, token);
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void updateUserEmail(String token, String username, String email) {
        ValidationUtils.isEmailValid(email);
        AppUser user = ValidateAuthorizationForAdminAndUser(username, token);
        if (user.getEmail().equals(email)) {
            throw new UserAlreadyExistsException("Same Email");
        }
        //Authenticate the new email via mail
        user.setEmail(email);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserUsername(String token, String username, String newUsername) {
        AppUser user = ValidateAuthorizationForAdminAndUser(username, token);
        if (user.getUsername().equals(newUsername)) {
            throw new UserAlreadyExistsException("Same Username");
        }
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    @Transactional
    public void changeUserPassword(String token, String username, String password) {
        AppUser user = ValidateAuthorizationForAdminAndUser(username, token);
        //Authenticate the new hashed_password via mail

        user.setHashed_password(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void changeUserRole(String token, String username, String roleInput) {
        ValidateAdmin(token);
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        user.setRole(Role.valueOf(roleInput));
        userRepository.save(user);
    }

    private AppUser ValidateAuthorizationForAdminAndUser(String username, String token) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);
        String usernameFromToken = jwtValidation.validateUsernameFromToken(token);

        if (!(roleFromToken.equals("ADMIN") || usernameFromToken.equals(username))) {
            throw new ForbiddenException("User does not have permission");
        }
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
    }

    private void ValidateAdmin(String token) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);

        if (!roleFromToken.equals("ADMIN")) {
            throw new ForbiddenException("User does not have permission");
        }
    }
}
