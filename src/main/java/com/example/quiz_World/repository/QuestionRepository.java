package com.example.quiz_World.repository;

import com.example.quiz_World.entities.quizEntity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByQuizIdAndQuestionNumber(Long id, Long number);
}
