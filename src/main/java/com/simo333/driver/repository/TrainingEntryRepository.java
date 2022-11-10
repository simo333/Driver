package com.simo333.driver.repository;

import com.simo333.driver.model.TrainingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingEntryRepository extends JpaRepository<TrainingEntry, Long> {
}
