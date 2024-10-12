package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<AppPerson, String> {

    @Query("SELECT p FROM AppPerson p WHERE p.username = ?1")
    Optional<AppPerson> findByUsername(String username);
}
