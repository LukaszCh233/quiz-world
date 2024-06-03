package com.example.quiz_World.repository;

import com.example.quiz_World.entities.quizEntity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByQuizIdAndQuestionNumber(Long id, Long number);
}
