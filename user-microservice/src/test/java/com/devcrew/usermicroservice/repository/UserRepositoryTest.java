package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        AppUser user = new AppUser("J123", "Juan@mail", "hashed_password");
        user.setAppPerson(null);
        userRepository.save(user);

        AppUser foundUser = userRepository.findByUsername("J123").orElse(null);
        assertNotNull(foundUser);
        assertEquals("J123", foundUser.getUsername());
    }
}