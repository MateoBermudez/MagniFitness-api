package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for the AppModule entity.
 */
public interface ModuleRepository extends JpaRepository<AppModule, Integer> {

    /**
     * Find an app module by its name.
     *
     * @param name the name of the app module
     * @return the app module
     */
    @Query("SELECT m FROM AppModule m WHERE m.name = ?1")
    Optional<AppModule> findByName(String name);
}
