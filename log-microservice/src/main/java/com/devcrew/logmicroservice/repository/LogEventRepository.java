package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventRepository extends JpaRepository<LogEvent, Integer> {
}
