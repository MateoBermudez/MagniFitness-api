package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the LogEvent entity.
 */
@Repository
public interface LogEventRepository extends JpaRepository<LogEvent, Integer> {
}
