package com.devcrew.usermicroservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

/**
 * Test class for RolePermissionController.
 * This class contains tests for the RolePermissionController endpoints.
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class RolePermissionControllerTest {

    /**
     * MockMvc instance for performing HTTP requests in tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * JwtService instance for generating JWT tokens.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Test for the getRolePermissions endpoint.
     * This test verifies that the endpoint returns the expected role permissions.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testGetRolePermissions() throws Exception {

        String token = getToken();

        MvcResult result = mockMvc.perform(get("/api/role-permission/get-all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertNotNull(content);
        Assertions.assertFalse(content.isEmpty());
        Assertions.assertTrue(content.contains("USER") && content.contains("READ") && content.contains("FULL_ACCESS"));
    }

    /**
     * Generates a JWT token for testing purposes.
     *
     * @return a JWT token as a String
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
