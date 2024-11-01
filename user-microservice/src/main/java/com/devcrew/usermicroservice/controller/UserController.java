package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Only admin can get all users
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/get-all")
    public ResponseEntity<Object> getUsers(@RequestHeader("Authorization") String token) {
        List<UserDTO> users = userService.getUsers(token);
        return ResponseEntity.ok(users);
    }

    //Only admin can get the info of any user, and user can get his own info
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @GetMapping(path = "info/{username}")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        UserDTO user = userService.getUserInfo(token, username);
        return ResponseEntity.ok(user);
    }

    //Only admin can delete any user, and user can delete his own account
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @DeleteMapping(path = "delete/{username}")
    public ResponseEntity<Object> deleteUser(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        userService.deleteUser(username, token);
        return ResponseEntity.noContent().build();
    }

    //Only admin can change the email of any user, and user can change his own email
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @PutMapping(path = "updateEmail/{username}")
    public ResponseEntity<Object> updateUserEmail(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                  @RequestParam() String email) {
        userService.updateUserEmail(token, username, email);
        return ResponseEntity.noContent().build();
    }

    //Only admin can change the username of any user, and user can change his own username
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @PutMapping(path = "updateUsername/{username}")
    public ResponseEntity<Object> updateUserUsername(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                     @RequestParam() String newUsername) {
        userService.updateUserUsername(token, username, newUsername);
        return ResponseEntity.noContent().build();
    }

    //Only admin can change the password of any user, and user can change his own password
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    @PutMapping(path = "changePassword/{username}")
    public ResponseEntity<Object> changePassword(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                 @RequestParam() String password) {
        userService.changeUserPassword(token, username, password);
        return ResponseEntity.noContent().build();
    }

    //Only admin can change the role of any user
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "changeRole/{username}")
    public ResponseEntity<Object> changeRole(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                             @RequestParam() String role) {
        userService.changeUserRole(token, username, role);
        return ResponseEntity.noContent().build();
    }
}