package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

        @Query("SELECT s FROM AppUser s WHERE s.email = ?1")
        Optional<AppUser> findByEmail(String email);

        @Query("SELECT s FROM AppUser s WHERE s.username = ?1")
        Optional<AppUser> findByUsername(String username);
}
