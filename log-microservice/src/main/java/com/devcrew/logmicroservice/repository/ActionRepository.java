package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for the Action entity.
 */
public interface ActionRepository extends JpaRepository<Action, Integer> {

    /**
     * Find an action by its name.
     *
     * @param name the name of the action
     * @return the action
     */
    @Query("SELECT a FROM Action a WHERE a.name = ?1")
    Optional<Action> findByName(String name);
}
