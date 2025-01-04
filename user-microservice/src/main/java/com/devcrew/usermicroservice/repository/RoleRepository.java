package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository interface extends JpaRepository for CRUD operations with Role entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Method to find Role by name
     *
     * @param name Role name
     * @return Optional Role object
     */
    @Query("SELECT r FROM Role r WHERE r.name = ?1")
    Optional<Role> findByName(String name);
}
