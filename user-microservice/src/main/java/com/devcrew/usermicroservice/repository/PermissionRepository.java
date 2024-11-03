package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.Permission;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PermissionRepository interface for managing Permission entities. (CRUD operations)
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    /**
     * Finds a Permission by its name.
     *
     * @param name the name of the permission
     * @return an Optional containing the found Permission, or empty if not found
     */
    @Query("SELECT p FROM Permission p WHERE p.name = ?1")
    Optional<Permission> findByName(String name);
}
