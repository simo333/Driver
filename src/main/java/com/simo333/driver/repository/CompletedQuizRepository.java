package com.simo333.driver.repository;

import com.simo333.driver.model.CompletedQuiz;
import com.simo333.driver.payload.completed_quiz.CompletedQuizResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Long> {

    @Query("SELECT q.timeStamp, q.user.id, q.advice.id, q.score, count(q.givenAnswers) FROM CompletedQuiz q WHERE q.user.id = ?1")
    Page<CompletedQuizResponse> findAllByUserId(Long userId, Pageable page);

    @Query("SELECT q.timeStamp, q.user.id, q.advice.id, q.score, count(q.givenAnswers) FROM CompletedQuiz q WHERE q.advice.id = ?1")
    Page<CompletedQuizResponse> findAllByAdviceId(Long adviceId, Pageable page);

    @Query("SELECT q.timeStamp, q.user.id, q.advice.id, q.score, count(q.givenAnswers) FROM CompletedQuiz q WHERE q.user.id = ?1 AND q.advice.id = ?2")
    List<CompletedQuizResponse> findAllByUserIdAndAdviceId(Long userId, Long adviceId);

    @Query("SELECT q.timeStamp, q.user.id, q.advice.id, max(q.score), count(q.givenAnswers) FROM CompletedQuiz q WHERE q.user.id = ?1 AND q.advice.id = ?2")
    Optional<CompletedQuizResponse> findTopByUserIdAndAdviceId(Long userId, Long adviceId);


}
