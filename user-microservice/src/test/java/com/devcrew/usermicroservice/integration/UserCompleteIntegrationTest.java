package com.devcrew.usermicroservice.integration;

import com.devcrew.usermicroservice.exception.UserDoesNotExistException;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCompleteIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;

    String newUserJson = """
                {
                    "user_name": "Ma123",
                    "mail": "mariam@gmail.com",
                    "password": "123456",
                    "authenticated": true,
                    "person": {
                        "user_real_name": "Mariam",
                        "user_last_name": "Gonzalez",
                        "user_date_of_birth": "1990-01-01",
                        "user_personal_Info": "I am a software engineer",
                        "user_age": 31
                    }
                }
            """;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        personRepository.deleteAll();
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson)).andExpect(status().isOk());
    }

    @Test
    void shouldCreateAndRetrieveUser() throws Exception {
        List<AppUser> users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("Ma123", users.get(0).getUsername());

        mockMvc.perform(get("/api/user/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("Ma123")));
    }

    @Test
    void shouldUpdateUserEmail() throws Exception {
        mockMvc.perform(put("/api/user/updateEmail/Ma123")
                        .param("email", "Ma123@mail.com"))
                .andExpect(status().isOk());

        AppUser updatedUser = userRepository.findByUsername("Ma123").orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        Assertions.assertEquals("Ma123@mail.com", updatedUser.getEmail());
    }

    @Test
    void shouldUpdateUserUsername() throws Exception {
        mockMvc.perform(put("/api/user/updateUsername/Ma123")
                        .param("oldUsername", "Ma123")
                        .param("newUsername", "newUser"))
                .andExpect(status().isOk());

        Optional<AppUser> updatedUser = userRepository.findByUsername("newUser");
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    void shouldChangePassword() throws Exception {
        mockMvc.perform(put("/api/user/changePassword/Ma123")
                        .param("password", "newPassword"))
                .andExpect(status().isOk());

        AppUser updatedUser = userRepository.findByUsername("Ma123").orElseThrow(
                () -> new UserDoesNotExistException("User does not exist")
        );
        Assertions.assertEquals("newPassword", updatedUser.getHashed_password());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/delete/Ma123"))
                .andExpect(status().isOk());

        List<AppUser> users = userRepository.findAll();
        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    void shouldGetUserInfo() throws Exception {
        mockMvc.perform(get("/api/user/info/Ma123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("Ma123")));
    }

    @Test
    void shouldGetUsers() throws Exception {
        mockMvc.perform(get("/api/user/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("Ma123")));
    }
}
