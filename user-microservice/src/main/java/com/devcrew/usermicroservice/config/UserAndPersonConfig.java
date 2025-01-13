package com.devcrew.usermicroservice.config;

import com.devcrew.usermicroservice.model.*;
import com.devcrew.usermicroservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static java.time.Month.*;

/**
 * UserAndPersonConfig is a configuration class
 * that sets up the database with some initial data to test the application.
 */
//This class is setting up the database with some initial data to test the application
@Configuration
public class UserAndPersonConfig {

    /**
     * Creates a CommandLineRunner bean that runs the application.
     * It sets up the database with some initial data to test the application.
     * It creates roles, permissions, users, and persons.
     * It also assigns roles and permissions to users.
     * It saves the data to the database.
     *
     * @param userRepository the UserRepository instance
     * @param personRepository the PersonRepository instance
     * @param roleRepository the RoleRepository instance
     * @param permissionRepository the PermissionRepository instance
     * @param rolePermissionRepository the RolePermissionRepository instance
     * @return the CommandLineRunner instance
     */
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PersonRepository personRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        return args -> {
            LocalDate dob1 = LocalDate.of(1990, JANUARY, 1);
            LocalDate dob2 = LocalDate.of(1995, JANUARY, 1);

            Role userRole = new Role(1, "USER");
            Role adminRole = new Role(2, "ADMIN");

            Permission permission1 = new Permission(1, "CREATE");
            Permission permission2 = new Permission(2, "READ");
            Permission permission3 = new Permission(3, "UPDATE");
            Permission permission4 = new Permission(4, "DELETE");
            Permission permission5 = new Permission(5, "ADMIN");
            Permission permission6 = new Permission(6, "FULL_ACCESS");

            roleRepository.saveAll(
                    List.of(
                            userRole,
                            adminRole
                    )
            );

            permissionRepository.saveAll(
                    List.of(
                            permission1,
                            permission2,
                            permission3,
                            permission4,
                            permission5,
                            permission6
                    )
            );

            rolePermissionRepository.saveAll(
                    List.of(
                            new RolePermission(userRole, permission1, "CREATE permission for user"),
                            new RolePermission(userRole, permission2, "READ permission for user"),
                            new RolePermission(userRole, permission3, "UPDATE permission for user"),
                            new RolePermission(userRole, permission4, "DELETE permission for user"),
                            new RolePermission(adminRole, permission1, "CREATE permission for admin"),
                            new RolePermission(adminRole, permission2, "READ permission for admin"),
                            new RolePermission(adminRole, permission3, "UPDATE permission for admin"),
                            new RolePermission(adminRole, permission4, "DELETE permission for admin"),
                            new RolePermission(adminRole, permission5, "ADMIN permission for admin"),
                            new RolePermission(adminRole, permission6, "FULL_ACCESS permission for admin")
                    )
            );

            AppUser user1 = new AppUser(
                    "Ma123",
                    "mariam@gmail.com",
                    false,
                    LocalDate.now(),
                    LocalDate.now(),
                    null, adminRole, null
            );

            user1.setLoggedIn(true);
            user1.setAuthenticated(true);
            user1.setTwoFactorAuthSecretKey("secret");

            AppUser user2 = new AppUser(
                    "Al123",
                    "alex@gmail.com",
                    false,
                    LocalDate.now(),
                    LocalDate.now(),
                    null, adminRole, null
            );

            user2.setLoggedIn(true);
            user2.setAuthenticated(true);
            user2.setTwoFactorAuthSecretKey("secret");

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
