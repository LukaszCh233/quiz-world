package com.example.quiz_World.repository;

import com.example.quiz_World.entities.Quiz;
import com.example.quiz_World.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findQuizzesByUserId(Long id);

    List<Quiz> findByStatus(Status status);

}
