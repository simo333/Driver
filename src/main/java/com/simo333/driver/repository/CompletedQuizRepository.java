package com.simo333.driver.repository;

import com.simo333.driver.model.CompletedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Long> {
}
