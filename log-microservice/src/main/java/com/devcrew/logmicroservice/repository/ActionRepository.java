package com.devcrew.logmicroservice.repository;

import com.devcrew.logmicroservice.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Integer> {
}
