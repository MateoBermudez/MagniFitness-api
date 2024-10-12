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
        AppUser user = new AppUser("J22", "Doe@Mail", "hashed_password");
        userRepository.save(user);

        AppUser foundUser = userRepository.findByEmail("Doe@Mail").orElse(null);
        assertNotNull(foundUser);
        assertEquals("J22", foundUser.getUsername());
    }
}
