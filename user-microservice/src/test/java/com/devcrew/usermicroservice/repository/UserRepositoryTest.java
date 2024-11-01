package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindUser() {
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
        assertEquals("J22", foundUser.getUsername());
    }
}
