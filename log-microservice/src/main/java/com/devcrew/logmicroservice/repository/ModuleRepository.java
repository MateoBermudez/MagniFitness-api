package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<AppModule, Integer> {
}
