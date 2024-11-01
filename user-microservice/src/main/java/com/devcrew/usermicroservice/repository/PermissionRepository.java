package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.Permission;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p WHERE p.name = ?1")
    Optional<Permission> findByName(String name);
}
