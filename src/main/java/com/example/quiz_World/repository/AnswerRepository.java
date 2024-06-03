package com.example.quiz_World.repository;

import com.example.quiz_World.entities.quizEntity.AnswerToQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerToQuiz, Long> {
}
