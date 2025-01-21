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
import com.devcrew.usermicroservice.utils.JsonBuilderUtils;
import com.devcrew.usermicroservice.utils.JwtValidation;
import com.devcrew.usermicroservice.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;


/**
 * Service class for managing user-related operations.
 * All the methods use the AuthorizationUtils class to validate the user's permissions to perform the operation.
 */
@Service
public class UserService {

    /**
     * Log sender service for sending logs to another service.
     */
    private final LogSenderService logSenderService;

    /**
     * User repository for accessing user data.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for encoding passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * JWT validation utility.
     */
    private final JwtValidation jwtValidation;

    /**
     * Role permission repository for accessing role-permission data.
     */
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Role repository for accessing role data.
     */
    private final RoleRepository roleRepository;

    /**
     * Constructor for UserService, injecting necessary dependencies.
     *
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     * @param jwtValidation the JWT validation utility
     * @param rolePermissionRepository the role permission repository
     * @param roleRepository the role repository
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtValidation jwtValidation,
                       RolePermissionRepository rolePermissionRepository,
                       RoleRepository roleRepository,
                       LogSenderService logSenderService) {
        this.logSenderService = logSenderService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtValidation = jwtValidation;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves a list of all users.
     *
     * @param token the JWT token of the user doing the operation
     * @return a list of UserDTO objects containing the user's information
     */
    public List<UserDTO> getUsers(String token) {
        return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
    }

    /**
     * Retrieves user information for a specific user.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the username of the user
     * @return a UserDTO object containing the user information
     */
    public UserDTO getUserInfo(String token, String username) {
        AppUser user = validatePermissions(username, token, "READ");
        return UserMapper.toDTO(user);
    }

    /**
     * Deletes a user.
     *
     * @param username the username of the user to delete
     * @param token the JWT token of the user doing the operation
     */
    @Transactional
    public void deleteUser(String username, String token) {
        try {
            AppUser user = validatePermissions(username, token, "DELETE");

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Delete", "User", "app_user", user.getId(),
                    "User with " + username + " username has been deleted successfully.",
                    JsonBuilderUtils.jsonBuilder(user),
                    "{}",
                    user.getUsername(),
                    user.getEmail()
            );

            userRepository.deleteById(user.getId());
        } catch (UserDoesNotExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Updates the email of a user.
     * It validates the Email and checks if the new email is the same as the old one.
     * It also validates the user's permissions to perform the operation.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the username of the user
     * @param email the new email
     */
    @Transactional
    public void updateUserEmail(String token, String username, String email) {
        try {
            ValidationUtils.isEmailValid(email);
            AppUser user = validatePermissions(username, token, "UPDATE");
            if (user.getEmail().equals(email)) {
                throw new UserAlreadyExistsException("Same Email");
            }

            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setEmail(email);

            String jsonAfter = JsonBuilderUtils.jsonBuilder(user);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + username + " username email has been changed successfully.",
                    jsonBefore,
                    jsonAfter,
                    user.getUsername(),
                    user.getEmail()
            );

            userRepository.save(user);
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Updates the username of a user.
     * It validates the user's permissions to perform the operation.
     * It also checks if the new username is the same as the old one and if the new username already exists.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the current username
     * @param newUsername the new username
     */
    @Transactional
    public void updateUserUsername(String token, String username, String newUsername) {
        try {
            AppUser user = validatePermissions(username, token, "UPDATE");
            if (user.getUsername().equals(newUsername)) {
                throw new UserAlreadyExistsException("Same Username");
            }
            if (userRepository.findByUsername(newUsername).isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            }

            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setUsername(newUsername);

            String jsonAfter = JsonBuilderUtils.jsonBuilder(user);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + username + " username has been changed successfully to " + newUsername + " username.",
                    jsonBefore,
                    jsonAfter,
                    user.getUsername(),
                    user.getEmail()
            );

            userRepository.save(user);
        } catch (UserAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the username of the user
     * @param password the new password
     */
    @Transactional
    public void changeUserPassword(String token, String username, String password) {
        try {
            AppUser user = validatePermissions(username, token, "UPDATE");

            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setHashed_password(passwordEncoder.encode(password));

            String jsonAfter = JsonBuilderUtils.jsonBuilder(user);

            userRepository.save(user);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + username + " username password has been changed successfully.",
                    jsonBefore,
                    jsonAfter,
                    user.getUsername(),
                    user.getEmail()
            );

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Changes the role of a user.
     * Only admins can change the role of a user.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the username of the user
     * @param roleInput the new role
     */
    @Transactional
    public void changeUserRole(String token, String username, String roleInput) {
        try {
            AppUser user = userRepository.findByUsername(username).orElseThrow(
                    () -> new UserDoesNotExistException("User does not exist")
            );
            Role role = roleRepository.findByName(roleInput).orElseThrow(
                    () -> new BadRequestException("Role does not exist")
            );

            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setRole(role);

            String jsonAfter = JsonBuilderUtils.jsonBuilder(user);

            userRepository.save(user);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + username + " username role has been changed successfully.",
                    jsonBefore,
                    jsonAfter,
                    user.getUsername(),
                    user.getEmail()
            );

        } catch (UserDoesNotExistException | BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Logs out a user of the system by setting the loggedIn field to false.
     *
     * @param token the JWT token of the user doing the operation
     * @param username the username of the user
     */
    @Transactional
    public void logout(String token, String username) {
        try {
            AppUser user = validatePermissions(username, token, "UPDATE");

            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setLoggedIn(false);
            userRepository.save(user);
            jwtValidation.invalidateToken(token);

            String jsonAfter = JsonBuilderUtils.jsonBuilder(user);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + username + " username has been logged out successfully.",
                    jsonBefore,
                    jsonAfter,
                    user.getUsername(),
                    user.getEmail()
            );

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Validates the permissions of a user
     * to perform an operation based on the permission needed to perform the operation.
     *
     * @param username the username of the user
     * @param token the JWT token of the user doing the operation
     * @param permissionNeeded the required permission
     * @return the AppUser object with the user's information
     */
    private AppUser validatePermissions(String username, String token, String permissionNeeded) {
        return AuthorizationUtils.validatePermissions(username, token, permissionNeeded, jwtValidation, userRepository, rolePermissionRepository);
    }

    /**
     * Validates if the user has admin permissions.
     *
     * @param token the JWT token of the user doing the operation
     */
    public boolean validateAdmin(String token) {
        return AuthorizationUtils.validateAdminPermissions(token, jwtValidation, userRepository, rolePermissionRepository);
    }


    /**
     * Saves a user to the database.
     * If the user already exists, it returns the user.
     * This is used for OAuth2 login.
     *
     * @param oAuth2User the user to save
     * @return the saved user
     */
    //Only Google is supported for now
    @Transactional
    public AppUser OAuth2Login(OAuth2User oAuth2User) {
        AppUser user;
        String emailAttribute = oAuth2User.getAttribute("email");

        user = userRepository.findByEmail(emailAttribute).orElse(null);
        // If the user already exists, log them in
        if (user != null) {
            sendLogForExistingOAuth2User(user);
            return user;
        }

        user = handleOAuth2Login(oAuth2User, emailAttribute);
        sendLogForCreatedOAuth2User(user);
        return user;
    }

    /**
     * Sends a log for an existing OAuth2 user.
     * @param user the user to send the log for
     */
    private void sendLogForExistingOAuth2User(AppUser user) {
        try {
            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);
            user.setLoggedIn(true);
            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + user.getUsername() + " username has logged in successfully using OAuth2.",
                    jsonBefore,
                    JsonBuilderUtils.jsonBuilder(user),
                    user.getUsername(),
                    user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Handles the OAuth2 login.
     * Saves the user to the database.
     * @param oAuth2User the OAuth2 user
     * @param email the email of the user
     * @return the saved user
     */
    private AppUser handleOAuth2Login(OAuth2User oAuth2User, String email) {
        AppUser user;
        try {
            String username = oAuth2User.getAttribute("name");
            username = (username != null) ? username.replaceAll("\\s", "") : null;
            user = userRepository.findByEmail(email).orElse(new AppUser());
            user.setEmail(email);
            user.setUsername(username);
            user.setRole(roleRepository.findByName("USER").orElse(null));
            user.setEnabled(true);
            user.setLoggedIn(true);

            user = userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    /**
     * Sends a log for a created OAuth2 user.
     * @param user the user to send the log for
     */
    private void sendLogForCreatedOAuth2User(AppUser user) {
        try {
            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Create", "User", "app_user", user.getId(),
                    "User with " + user.getUsername() + " username has been created successfully using OAuth2.",
                    JsonBuilderUtils.jsonBuilder("{}"),
                    JsonBuilderUtils.jsonBuilder(user),
                    user.getUsername(),
                    user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves the 2FA secret key to the database.
     * @param token the JWT token of the user doing the operation
     * @param encryptedSecretKey the encrypted 2FA secret key
     * @return the email of the user whose 2FA secret key was saved
     */
    @Transactional
    public String update2FASecretKey(String token, String encryptedSecretKey) {
        AppUser user = userRepository.findByUsername(jwtValidation.validateUsernameFromToken(token)).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        user.setTwoFactorAuthSecretKey(encryptedSecretKey);
        return userRepository.save(user).getEmail();
    }

    /**
     * Retrieves the 2FA secret key from the database.
     * @param token the JWT token of the user doing the operation
     * @return the 2FA secret key
     */
    public String get2FASecretKey(String token) {
        return userRepository.findByUsername(jwtValidation.validateUsernameFromToken(token)).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ).getTwoFactorAuthSecretKey();
    }

    /**
     * Updates the 2FA status of a user. If the status is true, the user is authenticated.
     * @param token the JWT token of the user doing the operation
     * @param faStatus the 2FA status
     */
    @Transactional
    public void updateUser2FAStatus(String token, boolean faStatus) {
        AppUser user = userRepository.findByUsername(jwtValidation.validateUsernameFromToken(token)).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        try {
            String jsonBefore = JsonBuilderUtils.jsonBuilder(user);

            user.setAuthenticated(faStatus);

            logSenderService.mapAndSendLog(
                    null, null, null,
                    "Update", "User", "app_user", user.getId(),
                    "User with " + user.getUsername() + " username has been authenticated successfully.",
                    jsonBefore,
                    JsonBuilderUtils.jsonBuilder(user),
                    user.getUsername(),
                    user.getEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userRepository.save(user);
    }

    /**
     * Resets the 2FA status of a user. Used for sensitive operations and to check the user's identity.
     * @param token the JWT token of the user doing the operation
     * @param faStatus the 2FA status to reset to
     */
    @Transactional
    public void reset2FA(String token, boolean faStatus) {
        AppUser user = userRepository.findByUsername(jwtValidation.validateUsernameFromToken(token)).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        user.setAuthenticated(faStatus); // Normally, this should be false
        user.setTwoFactorAuthSecretKey(null); // Reset the 2FA secret key
        userRepository.save(user);
    }

    /**
     * Retrieves the user information from a valid token.
     * @param token the JWT token of the user making the request
     * @return a UserDTO object with information about the user
     */
    public UserDTO getUserFromValidToken(String token) {
        return UserMapper.toDTO(userRepository.findByUsername(jwtValidation.validateUsernameFromToken(token)).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        ));
    }

    /**
     * Validates if the token holder is a user.
     * Jwt Validation makes a call to the database to check if the user exists,
     * so it's not necessary to call the database again.
     * @param token the JWT token of the user making the request
     * @return true if the token holder is a user, false otherwise
     */
    public boolean validateUser(String token) {
        return jwtValidation.validateUsernameFromToken(token) != null;
    }
}