package com.example.quiz_World.repository;

import com.example.quiz_World.entities.Quiz;
import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.WordSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    void deleteByQuiz(Quiz quiz);

    void deleteByWordSet(WordSet wordSet);

    List<Result> findByUserId(Long id);

    Result findByUserIdAndWordSetId(Long userId, Long wordSetId);

    Result findByUserIdAndQuizId(Long userId, Long quizId);
}
