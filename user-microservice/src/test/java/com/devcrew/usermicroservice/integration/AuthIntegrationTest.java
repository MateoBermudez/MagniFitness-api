package com.devcrew.usermicroservice.integration;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import static java.time.Month.JANUARY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test class for authentication-related endpoints.
 * This class contains tests for login and registration endpoints.
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class AuthIntegrationTest {
    /**
     * MockMvc instance for performing HTTP requests in tests.
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * Repository for managing AppPerson entities.
     */
    @Autowired
    private PersonRepository personRepository;
    /**
     * Repository for managing AppUser entities.
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * ObjectMapper instance for JSON processing.
     */
    @Autowired
    private ObjectMapper objectMapper;
    /**
     * Sets up the test environment before each test.
     * Deletes all entries in the repositories.
     */
    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Internal API key for accessing the endpoints.
     */
    @Value("${internal.api.key}")
    private String apiKey;

    /**
     * Test for the login endpoint.
     * This test verifies that a valid login request returns a token.
     * The test also sets up the initial state by saving a test user in the repository.
     * The test user is the same as the one used in the login request.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void loginTest() throws Exception {
        SaveUser();
        String jsonLogin = """
                {
                  "identifier": "Ma123",
                  "password": "123"
                }
                """;
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .header("X-API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("token").asText();
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }
    /**
     * Test for the registration endpoint.
     * This test verifies that a valid registration request returns a token and saves the user.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void registerTest() throws Exception{
        String jsonRegister = """
                {
                  "user_name": "Eldest",
                  "mail": "user@gmail.com",
                  "password": "Mdkjvjdkj293845dmfkvj@@34!!",
                  "authenticated": false,
                  "person": {
                    "user_real_name": "Julian",
                    "user_last_name": "Alvarez",
                    "user_date_of_birth": "2010-01-01",
                    "user_personal_Info": "I am a Student",
                    "user_age": 14
                  }
                }
                """;
        MvcResult result = mockMvc.perform(post("/auth/register")
                        .header("X-API-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRegister))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("token").asText();
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
        Optional<AppUser> user = userRepository.findByUsername("Eldest");
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("Julian", user.get().getAppPerson().getName());
    }
    /**
     * Saves a test user in the repository.
     * This method is used to set up the initial state for the login test.
     */
    private void SaveUser() {
        LocalDate dob1 = LocalDate.of(1990, JANUARY, 1);
        Role adminRole = new Role(2, "ADMIN");
        AppUser user1 = new AppUser(
                "Ma123",
                "mariam@gmail.com",
                false,
                LocalDate.now(),
                LocalDate.now(),
                null, adminRole, null
        );
        user1.setEnabled(true);
        AppPerson person1 = new AppPerson(
                "Mariam",
                "Gonzalez",
                dob1,
                "Some personal info",
                Period.between(dob1, LocalDate.now()).getYears(),
                user1
        );
        user1.setAppPerson(person1);
        user1.setHashed_password(new BCryptPasswordEncoder().encode("123"));
        personRepository.save(person1);
        userRepository.save(user1);
    }
}