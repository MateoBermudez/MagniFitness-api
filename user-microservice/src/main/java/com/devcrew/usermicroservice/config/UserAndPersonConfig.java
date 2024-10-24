package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.model.AppPerson;
import com.devcrew.usermicroservice.model.AppUser;
import com.devcrew.usermicroservice.model.Role;
import com.devcrew.usermicroservice.repository.PersonRepository;
import com.devcrew.usermicroservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static java.time.Month.*;

//This class is setting up the database with some initial data to test the application
@Configuration
public class UserAndPersonConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PersonRepository personRepository) {
        return args -> {
            LocalDate dob1 = LocalDate.of(1990, JANUARY, 1);
            LocalDate dob2 = LocalDate.of(1995, JANUARY, 1);

            AppUser user1 = new AppUser(
                    "Ma123",
                    "mariam@gmail.com",
                    false,
                    LocalDate.now(),
                    LocalDate.now(),
                    null, Role.ADMIN
            );

            AppUser user2 = new AppUser(
                    "Al123",
                    "alex@gmail.com",
                    false,
                    LocalDate.now(),
                    LocalDate.now(),
                    null, Role.ADMIN
            );

            user1.setHashed_password(new BCryptPasswordEncoder().encode("123"));

            user2.setHashed_password(new BCryptPasswordEncoder().encode("123"));

            AppPerson person1 = new AppPerson(
                    "Mariam",
                    "Gonzalez",
                    dob1,
                    "Some personal info",
                    Period.between(dob1, LocalDate.now()).getYears(),
                     user1
            );

            AppPerson person2 = new AppPerson(
                    "Alex",
                    "Gonzalez",
                    dob2,
                    "Some personal info",
                    Period.between(dob2, LocalDate.now()).getYears(),
                    user2
            );

            user1.setAppPerson(person1);
            user2.setAppPerson(person2);

            user1.setEnabled(true);
            user2.setEnabled(true);

            personRepository.saveAll(
                    List.of(person1, person2)
            );

            userRepository.saveAll(
                    List.of(user1, user2)
            );
        };
    }
}
