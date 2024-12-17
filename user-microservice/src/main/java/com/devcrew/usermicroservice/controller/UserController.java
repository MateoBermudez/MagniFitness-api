package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.dto.UserDTO;
import com.devcrew.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is the controller class for the User entity.
 * It contains the endpoints for the User entity.
 * The endpoints are used to get, add, update, and delete a user.
 * The endpoints are secured based on the role of the user.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * The user service is used to perform operations on the user entity.
     */
    private final UserService userService;

    /**
     * The constructor is used to inject the user service into the user controller.
     * @param userService The user service to be injected into the user controller.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This endpoint is used to get all the users in the system.
     *
     * @param token The token of the user making the request.
     * @return A response entity containing the list of users in the system.
     */
    //Only admin can get all users
    @GetMapping(path = "/get-all")
    public ResponseEntity<Object> getUsers(@RequestHeader("Authorization") String token) {
        List<UserDTO> users = userService.getUsers(token);
        return ResponseEntity.ok(users);
    }

    /**
     * This endpoint is used to get the information of a user.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user whose information is to be retrieved.
     * @return A response entity containing the information of the user.
     */
    //Only admin can get the info of any user, and user can get his own info
    @GetMapping(path = "info/{username}")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        UserDTO user = userService.getUserInfo(token, username);
        return ResponseEntity.ok(user);
    }

    /**
     * This endpoint is used to delete a user from the system.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user to be deleted.
     * @return A response entity indicating that the user has been deleted from the system.
     */
    //Only admin can delete any user, and user can delete his own account
    @DeleteMapping(path = "delete/{username}")
    public ResponseEntity<Object> deleteUser(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        userService.deleteUser(username, token);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to update the email of a user.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user whose email is to be updated.
     * @param email The new email of the user.
     * @return A response entity indicating that the email of the user has been updated.
     */
    //Only admin can change the email of any user, and user can change his own email
    @PutMapping(path = "updateEmail/{username}")
    public ResponseEntity<Object> updateUserEmail(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                  @RequestParam() String email) {
        userService.updateUserEmail(token, username, email);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to update the username of a user.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user whose username is to be updated.
     * @param newUsername The new username of the user.
     * @return A response entity indicating that the username of the user has been updated.
     */
    //Only admin can change the username of any user, and user can change his own username
    @PutMapping(path = "updateUsername/{username}")
    public ResponseEntity<Object> updateUserUsername(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                     @RequestParam() String newUsername) {
        userService.updateUserUsername(token, username, newUsername);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to change the password of a user.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user whose password is to be changed.
     * @param password The new password of the user.
     * @return A response entity indicating that the password of the user has been changed.
     */
    //Only admin can change the password of any user, and user can change his own password
    @PutMapping(path = "changePassword/{username}")
    public ResponseEntity<Object> changePassword(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                 @RequestParam() String password) {
        userService.changeUserPassword(token, username, password);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to change the role of a user.
     * Only admin can change the role of any user.
     *
     * @param token The token of the user making the request.
     * @param username The username of the user whose role is to be changed.
     * @param role The new role of the user.
     * @return A response entity indicating that the role of the user has been changed.
     */
    //Only admin can change the role of any user
    @PutMapping(path = "changeRole/{username}")
    public ResponseEntity<Object> changeRole(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                             @RequestParam() String role) {
        userService.changeUserRole(token, username, role);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "validate-admin")
    public ResponseEntity<Object> validateAdmin(@RequestHeader("Authorization") String token) {
        boolean isAdmin = userService.validateAdmin(token);
        return ResponseEntity.ok(isAdmin);
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        userService.logout(token, username);
        return ResponseEntity.noContent().build();
    }
}