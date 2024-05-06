package com.example.quiz_World.repository;

import com.example.quiz_World.entities.quizEntity.AnswerToQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerToQuiz, Long> {
}