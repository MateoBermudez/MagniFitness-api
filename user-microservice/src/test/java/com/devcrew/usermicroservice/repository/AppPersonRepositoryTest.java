package com.devcrew.usermicroservice.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;

@SpringBootTest
public class AppPersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindPerson() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        int age = Period.between(dob, LocalDate.now()).getYears();
        AppUser user = new AppUser("J22", "J@mail.com", "hashed_password", false, null);
        AppPerson person = new AppPerson("J22", "John", "Doe", LocalDate.of(1990, 1, 1), "Hello", age, user);
        user.setAppPerson(person);

        //userRepository goes first always because it is a strong entity, and person is dependent on user
        userRepository.save(user);
        personRepository.save(person);

        AppUser foundUser = userRepository.findByUsername("J22").orElse(null);
        AppPerson foundPerson = personRepository.findByUsername("J22").orElse(null);
        assertNotNull(foundPerson);
        assertNotNull(foundUser);
        assertEquals("John", foundPerson.getName());
        assertEquals("J22", foundUser.getUsername());
        assertEquals("J22", foundPerson.getUsername());
    }
}
