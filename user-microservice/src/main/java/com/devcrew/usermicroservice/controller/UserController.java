package com.devcrew.usermicroservice.controller;

import com.devcrew.usermicroservice.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.devcrew.usermicroservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    //Redirects post-requests to PersonController to add a new person
    private final PersonController personController;

    @Autowired
    public UserController(UserService userService, PersonController personController) {
        this.userService = userService;
        this.personController = personController;
    }

    @GetMapping(path = "/get-all")
    public List<AppUser> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "info/{username}")
    public AppUser getUser(@PathVariable("username") String username) {
        return userService.getUserInfo(username);
    }

    @PostMapping(path = "/register")
    public void registerNewUser(@RequestBody AppUser user) {
        userService.addNewUser(user);
        personController.addPerson(user.getAppPerson());
    }

    @DeleteMapping(path = "delete/{username}")
    public void deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
    }

    @PutMapping(path = "updateEmail/{username}")
    public void updateUserEmail(@PathVariable("username") String username,
                           @RequestParam() String email) {
        userService.updateUserEmail(username, email);
    }

    @PutMapping(path = "updateUsername/{username}")
    public void updateUserUsername(@PathVariable("username") String username,
                           @RequestParam() String newUsername) {
        userService.updateUserUsername(username, newUsername);
    }

    @PutMapping(path = "changePassword/{username}")
    public void changePassword(@PathVariable("username") String username,
                           @RequestParam() String password) {
        userService.changeUserPassword(username, password);
    }
}
