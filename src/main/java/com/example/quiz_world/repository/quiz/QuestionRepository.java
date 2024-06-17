package com.example.quiz_world.repository.quiz;

import com.example.quiz_world.entities.quizEntity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByQuizIdAndQuestionNumber(Long id, Long number);
}
