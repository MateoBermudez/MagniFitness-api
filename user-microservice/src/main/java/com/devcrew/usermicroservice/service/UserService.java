package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.UserMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.RoleRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.AuthorizationUtils;
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
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtValidation jwtValidation, RolePermissionRepository rolePermissionRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtValidation = jwtValidation;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserDTO> getUsers(String token) {
        validateAdminPermissions(token);
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    public UserDTO getUserInfo(String token, String username) {
        AppUser user = validatePermissions(username, token, "READ");
        return UserMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(String username, String token) {
        try {
            AppUser user = validatePermissions(username, token, "DELETE");
            userRepository.deleteById(user.getId());
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void updateUserEmail(String token, String username, String email) {
        try {
            ValidationUtils.isEmailValid(email);
            AppUser user = validatePermissions(username, token, "WRITE, EDIT, UPDATE");
            if (user.getEmail().equals(email)) {
                throw new UserAlreadyExistsException("Same Email");
            }
            user.setEmail(email);
            userRepository.save(user);
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void updateUserUsername(String token, String username, String newUsername) {
        try {
            AppUser user = validatePermissions(username, token, "WRITE, EDIT, UPDATE");
            if (user.getUsername().equals(newUsername)) {
                throw new UserAlreadyExistsException("Same Username");
            }
            if (userRepository.findByUsername(newUsername).isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            }
            user.setUsername(newUsername);
            userRepository.save(user);
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void changeUserPassword(String token, String username, String password) {
        try {
            AppUser user = validatePermissions(username, token, "WRITE, EDIT, UPDATE");
            user.setHashed_password(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void changeUserRole(String token, String username, String roleInput) {
        try {
            validatePermissions(username, token, "EDIT, UPDATE");
            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UserDoesNotExistException("User does not exist")
            );
            Role role = roleRepository.findByName(roleInput).orElseThrow(
                    () -> new BadRequestException("Role does not exist")
            );
            user.setRole(role);
            userRepository.save(user);
        } catch (UserDoesNotExistException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private AppUser validatePermissions(String username, String token, String permissionNeeded) {
        return AuthorizationUtils.validatePermissions(username, token, permissionNeeded, jwtValidation, userRepository, rolePermissionRepository);
    }

    private void validateAdminPermissions(String token) {
        AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
    }
}