package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<AppModule, Integer> {

    @Query("SELECT m FROM AppModule m WHERE m.name = ?1")
    Optional<AppModule> findByName(String name);
}
