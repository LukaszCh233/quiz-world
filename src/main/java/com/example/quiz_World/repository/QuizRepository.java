package com.example.quiz_World.repository;

import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.quizEntity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long id);

    List<Quiz> findByStatus(Status status);

    List<Quiz> findByQuizCategoryId(Long id);


}
