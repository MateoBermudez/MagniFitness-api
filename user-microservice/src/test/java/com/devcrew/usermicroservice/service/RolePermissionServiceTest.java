package com.devcrew.usermicroservice.service;

import com.devcrew.usermicroservice.config.UserAndPersonConfig;
import com.devcrew.usermicroservice.dto.RolePermissionDTO;
import com.devcrew.usermicroservice.model.*;
import com.devcrew.usermicroservice.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RolePermissionService.
 * This class contains tests for the role permission service.
 */
@ActiveProfiles("test")
@SpringBootTest
@Import({UserAndPersonConfig.class})
public class RolePermissionServiceTest {

    /**
     * Repository for managing RolePermission entities.
     */
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    /**
     * Service for managing a role and permission-related operations.
     */
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * Service for managing JWT tokens.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Test for retrieving role permissions.
     * This test verifies that the role permissions are retrieved correctly.
     */
    @Test
    public void testGetRolePermissions() {
        List<RolePermissionDTO> rolePermissionDTOList = rolePermissionService.getRolePermissions();

        assertNotNull(rolePermissionDTOList);
        assertFalse(rolePermissionDTOList.isEmpty());
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        assertEquals(rolePermissions.size(), rolePermissionDTOList.size());
    }

    /**
     * Generates a JWT token for testing.
     * This method creates a test user and generates a token for the user.
     * The user is admin, because only admins can access the role permissions service.
     *
     * @return the generated JWT token
     */
    private String getToken() {
        AppUser user = new AppUser(
                "Ma123",
                "mariam@gmail.com",
                false,
                LocalDate.now(),
                LocalDate.now(),
                null, new Role("ADMIN"), null
        );

        return (jwtService.getToken(user));
    }
}
