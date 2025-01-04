package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for the AppEntity entity.
 */
public interface AppEntityRepository extends JpaRepository<AppEntity, Integer> {

    /**
     * Find an app entity by its name.
     *
     * @param name the name of the app entity
     * @return the app entity
     */
    @Query("SELECT e FROM AppEntity e WHERE e.name = ?1")
    Optional<AppEntity> findByName(String name);
}
