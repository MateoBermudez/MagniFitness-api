package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.exception.UserAlreadyExistsException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserInfo(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void addNewUser(AppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userRepository.save(user);
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
        if (user.getUsername().equals(username)) {
            throw new UserAlreadyExistsException("Same Username");
        }
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setUsername(newUsername);
        userRepository.deleteById(user.getId());
        userRepository.save(user);
    }

    @Transactional
    public void changeUserPassword(String username, String password) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        //Authenticate the new hashed_password via mail
        user.setHashed_password(password);
        userRepository.save(user);
    }

}
