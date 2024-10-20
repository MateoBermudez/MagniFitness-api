package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.exception.BadRequestException;
import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.mapper.UserMapper;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    public UserDTO getUserInfo(String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        return UserMapper.toDTO(user);
    }

    @Transactional
    public void deleteUser(String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void updateUserEmail(String username, String email) {
        if (!ValidationUtils.isEmailValid(email)) {
            throw new BadRequestException("Invalid email");
        }

        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        if (user.getEmail().equals(email)) {
            throw new UserAlreadyExistsException("Same Email");
        }
        //Authenticate the new email via mail
        user.setEmail(email);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserUsername(String username, String newUsername) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
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
    public void changeUserPassword(String username, String password) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        //Authenticate the new hashed_password via mail

        user.setHashed_password(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void changeUserRole(String username, String roleInput) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        user.setRole(Role.valueOf(roleInput));
        userRepository.save(user);
    }
}
