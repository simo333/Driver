package com.simo333.driver.repository;

import com.simo333.driver.model.TrainingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public interface TrainingEntryRepository extends JpaRepository<TrainingEntry, Long> {
}
