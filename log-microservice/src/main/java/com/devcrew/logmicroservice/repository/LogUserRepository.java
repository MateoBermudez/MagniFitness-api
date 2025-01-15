package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.LogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogUserRepository extends JpaRepository<LogUser, Integer> {

    @Query("SELECT l FROM LogUser l WHERE l.username = :username")
    Optional<LogUser> findByUsername(String username);

    @Query("SELECT l FROM LogUser l WHERE l.email = :email")
    Optional<LogUser> findByEmail(String email);
}
