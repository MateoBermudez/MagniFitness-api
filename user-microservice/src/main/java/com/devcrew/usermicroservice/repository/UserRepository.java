package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository interface extends JpaRepository for CRUD operations with AppUser entity
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

        /**
         * Method to find AppUser by email
         *
         * @param email AppUser email
         * @return Optional AppUser object with User information if found, empty Optional otherwise
         */
        @Query("SELECT s FROM AppUser s WHERE s.email = ?1")
        Optional<AppUser> findByEmail(String email);

        /**
         * Method to find AppUser by username
         *
         * @param username AppUser username
         * @return Optional AppUser object with User information if found, empty Optional otherwise
         */
        @Query("SELECT s FROM AppUser s WHERE s.username = ?1")
        Optional<AppUser> findByUsername(String username);
}
