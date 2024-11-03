package com.devcrew.usermicroservice.repository;

import com.devcrew.usermicroservice.model.AppPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PersonRepository interface extends JpaRepository to use the CRUD operations for the AppPerson entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<AppPerson, Integer> {
}
