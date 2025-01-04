package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.UnauthorizedException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.UserRepository;

/**
 * This class is used to validate the permissions of a user.
 * It checks if the user has the required permission to perform an action.
 * It checks the permissions based on the role of the user, the permissions
 * assigned to the role and some permissions needed to perform an action.
 * It also checks if the user is trying to access his own information.
 * It also checks if the user is an admin.
 */
public class AuthorizationUtils {

    /**
     * Admin role and permission
     */
    private static final String ADMIN = "ADMIN";

    /**
     * Full access permission
     */
    private static final String FULL_ACCESS = "FULL_ACCESS";

    /**
     * User role
     */
    private static final String USER = "USER";


    /**
     * This method is used to validate the permissions of a user.
     * It checks if the user has the required permission to perform an action.
     * It checks the permissions based on the role of the user, one of the user's permissions
     * needs to be in the permissions needed String to perform an action.
     * It also checks if the user is trying to access his own information.
     *
     * @param username The username of the user
     * @param token The token of the user
     * @param permissionNeeded The permission needed to perform an action
     * @param jwtValidation The jwtValidation object
     * @param userRepository The userRepository object
     * @param rolePermissionRepository The rolePermissionRepository object
     * @return The object with the user information
     */
    public static AppUser validatePermissions(String username, String token, String permissionNeeded, JwtValidation jwtValidation, UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);
        String usernameFromToken = jwtValidation.validateUsernameFromToken(token);
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        String permissions = String.valueOf(rolePermissionRepository.findByRole(user.getRole().getId()));

        if (!hasPermission(roleFromToken, usernameFromToken, username, permissions, permissionNeeded)) {
            throw new UnauthorizedException("User does not have permission");
        }
        return user;
    }


    /**
     * This method is used to validate the permissions of a user,
     * which needs to be an admin, or have Full Access permission.
     *
     * @param token The token of the user
     * @param jwtValidation The jwtValidation object
     * @param userRepository The userRepository object
     * @param rolePermissionRepository The rolePermissionRepository object
     */
    public static boolean validateAdminPermissions(String token, JwtValidation jwtValidation, UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);
        String usernameFromToken = jwtValidation.validateUsernameFromToken(token);
        AppUser user = userRepository.findByUsername(usernameFromToken).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        String permissions = String.valueOf(rolePermissionRepository.findByRole(user.getRole().getId()));

        if (!hasAdminPermission(roleFromToken, permissions)) {
            throw new UnauthorizedException("User does not have permission");
        }

        return true;
    }


    /**
     * This method is used to validate the permissions of a user.
     * A user can only read and edit his own information unless he is an admin or has Full Access permission.
     *
     * @param roleFromToken The role of the user extracted from the token.
     * @param usernameFromToken The username extracted from the token.
     * @param username The username being validated, it does not come from the token but the request.
     * @param permissions The permissions assigned to the role of the user.
     * @param permissionNeeded The permission needed to perform an action.
     * @return true if the user has the required permission, false otherwise.
     */
    private static boolean hasPermission(String roleFromToken, String usernameFromToken, String username, String permissions, String permissionNeeded) {
        // A user can only read and edit his own information
        if (roleFromToken.equals(USER) && !usernameFromToken.equals(username)) {
            return false;
        }
        return roleFromToken.equals(ADMIN) || permissions.contains(permissionNeeded)
                || permissions.contains(ADMIN) || permissions.contains(FULL_ACCESS);
    }

    /**
     * This method is used to validate if a user has admin permissions.
     *
     * @param roleFromToken The role of the user extracted from the token.
     * @param permissions The permissions assigned to the role of the user.
     * @return true if the user has admin permissions, false otherwise.
     */
    private static boolean hasAdminPermission(String roleFromToken, String permissions) {
        return roleFromToken.equals(ADMIN) || permissions.contains(ADMIN) || permissions.contains(FULL_ACCESS);
    }
}