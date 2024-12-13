package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.LogEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LogEventRepository extends JpaRepository<LogEvent, Integer> {

    @Query("SELECT l FROM LogEvent l WHERE l.creationDate BETWEEN :startDate AND :endDate")
    Page<LogEvent> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
