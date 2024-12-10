package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppEntityRepository extends JpaRepository<AppEntity, Integer> {

    @Query("SELECT e FROM AppEntity e WHERE e.name = ?1")
    Optional<AppEntity> findByName(String name);
}
