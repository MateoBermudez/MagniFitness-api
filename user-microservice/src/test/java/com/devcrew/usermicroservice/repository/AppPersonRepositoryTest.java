package com.devcrew.usermicroservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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

    @Test
    public void testSaveAndFindPerson() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        AppUser user = new AppUser("J22", "J@mail.com", "hashed_password", true, null, null, null);
        AppPerson person = new AppPerson("John", "Doe", dob, "Some personal info", 31, user);
        user.setAppPerson(person);

        //userRepository goes first always because it is a strong entity, and person is dependent on user
        userRepository.save(user);
        personRepository.save(person);

        AppUser foundUser = userRepository.findByUsername("J22").orElse(null);
        assertNotNull(foundUser);
        AppPerson foundPerson = personRepository.findById(foundUser.getAppPerson().getId()).orElse(null);
        assertNotNull(foundPerson);
        assertEquals("John", foundPerson.getName());
        assertEquals("J22", foundUser.getUsername());
    }
}
