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

@ActiveProfiles("test")
@SpringBootTest
public class AppPersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Deprecated
    @Test
    public void testSaveAndFindPerson() {
//        LocalDate dob = LocalDate.of(1990, 1, 1);
//        AppUser user = new AppUser("J22", "J@mail.com", "hashed_password", true, null, null, null, Role.ADMIN);
//        AppPerson person = new AppPerson("John", "Doe", dob, "Some personal info", 31, user);
//        user.setAppPerson(person);
//
//        userRepository.save(user);
//        personRepository.save(person);
//
//        AppUser foundUser = userRepository.findByUsername("J22").orElse(null);
//        assertNotNull(foundUser);
//        AppPerson foundPerson = personRepository.findById(foundUser.getAppPerson().getId()).orElse(null);
//        assertNotNull(foundPerson);
//        assertEquals("John", foundPerson.getName());
//        assertEquals("J22", foundUser.getUsername());
    }

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
