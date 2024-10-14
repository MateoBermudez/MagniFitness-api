package com.devcrew.usermicroservice.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonCompleteIntegrationTest {

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
    void shouldGetPeople() throws Exception {
        mockMvc.perform(get("/api/person/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_real_name", is("Mariam")));
    }

    @Test
    void shouldGetPerson() throws Exception {
        mockMvc.perform(get("/api/person/info/Ma123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_real_name", is("Mariam")));
    }

    @Test
    void shouldUpdatePersonInformation() throws Exception {
        String updatedUserJson = """
                {
                       "user_real_name": "Manuela",
                       "user_last_name": "Martinez",
                       "user_date_of_birth": "2000-10-10",
                       "user_personal_Info": "I am a Gamer",
                       "user_age": 24
                }
            """;
        mockMvc.perform(put("/api/person/update/Ma123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedUserJson)).andExpect(status().isOk());
        mockMvc.perform(get("/api/person/info/Ma123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_personal_Info", is("I am a Gamer")))
                .andExpect(jsonPath("$.user_date_of_birth", is("2000-10-10")))
                .andExpect(jsonPath("$.user_age", is(24)))
                .andExpect(jsonPath("$.user_real_name", is("Manuela")))
                .andExpect(jsonPath("$.user_last_name", is("Martinez")));
    }
}
