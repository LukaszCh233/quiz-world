package com.example.quiz_world.quiz.quiz;

import com.example.quiz_world.account.user.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long id);

    List<Quiz> findByStatus(Status status);

    List<Quiz> findByQuizCategoryId(Long id);
}
