package com.example.quiz_world.quiz.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizQuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByQuizIdAndQuestionNumber(Long id, Long number);
}
