package com.devcrew.usermicroservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

/**
 * Test class for the AppPersonRepository.
 */
@ActiveProfiles("test")
@SpringBootTest
public class AppPersonRepositoryTest {

    /**
     * The person repository to be tested,
     * it can access the database and perform CRUD operations with the AppPerson entity.
     */
    @Autowired
    private PersonRepository personRepository;

    /**
     * The user repository to be tested,
     * it can access the database and perform CRUD operations with the AppUser entity.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Set up the test environment by deleting all the data from the repositories.
     */
    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Test the save and find methods of the person and user repositories.
     * The test creates a user and a person, saves them to the database and then finds the user by its username.
     * The test checks if the user was found and if the username is correct.
     * The test checks if the person was found and if the name is correct.
     * The test checks if the user and person are connected.
     */
    @Test
    public void testSaveAndFindPerson() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Role role = new Role(1, "ADMIN");
        AppUser user = new AppUser("J22", "J@mail.com", true, null, null, null, role, null);
        AppPerson person = new AppPerson("John", "Doe", dob, "Some personal info", 31, user);
        user.setAppPerson(person);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setHashed_password(passwordEncoder.encode("1234"));

        userRepository.save(user);
        personRepository.save(person);

        AppUser foundUser = userRepository.findByUsername("J22").orElse(null);
        assertNotNull(foundUser);
        AppPerson foundPerson = personRepository.findById(foundUser.getAppPerson().getId()).orElse(null);
        assertNotNull(foundPerson);
        assertEquals("John", foundPerson.getName());
        assertEquals("J22", foundUser.getUsername());
    }

    /**
     * Test the password encoder to encode and match passwords.
     * The test encodes a password and then checks if the encoded password matches the raw password.
     */
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "1234";

        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertThat(matches).isTrue();
    }
}
