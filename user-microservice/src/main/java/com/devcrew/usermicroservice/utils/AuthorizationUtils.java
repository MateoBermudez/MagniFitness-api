package com.devcrew.usermicroservice.utils;

import com.devcrew.usermicroservice.exception.UnauthorizedException;
import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.RolePermissionRepository;
import com.devcrew.usermicroservice.repository.UserRepository;

public class AuthorizationUtils {

    private static final String ADMIN = "ADMIN";
    private static final String FULL_ACCESS = "FULL_ACCESS";
    private static final String USER = "USER";

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

    public static void validateAdminPermissions(String token, JwtValidation jwtValidation, UserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        String roleFromToken = jwtValidation.validateRoleFromToken(token);
        String usernameFromToken = jwtValidation.validateUsernameFromToken(token);
        AppUser user = userRepository.findByUsername(usernameFromToken).orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );

        String permissions = String.valueOf(rolePermissionRepository.findByRole(user.getRole().getId()));

        if (!hasAdminPermission(roleFromToken, permissions)) {
            throw new UnauthorizedException("User does not have permission");
        }
    }

    private static boolean hasPermission(String roleFromToken, String usernameFromToken, String username, String permissions, String permissionNeeded) {
        // A user can only read and edit his own information
        if (roleFromToken.equals(USER) && !usernameFromToken.equals(username)) {
            return false;
        }
        return roleFromToken.equals(ADMIN) || permissions.contains(permissionNeeded)
                || permissions.contains(ADMIN) || permissions.contains(FULL_ACCESS);
    }

    private static boolean hasAdminPermission(String roleFromToken, String permissions) {
        return roleFromToken.equals(ADMIN) || permissions.contains(ADMIN) || permissions.contains(FULL_ACCESS);
    }
}