package com.simo333.driver.repository;

import com.simo333.driver.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    Set<Question> findRandom(@Param("limit") int numberOfQuizQuestions);
}
