package com.example.quiz_world.quiz.quizCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Long> {
    @Query("SELECT q FROM QuizCategory q WHERE LOWER(q.name) = LOWER(:name)")
    Optional<QuizCategory> findByNameIgnoreCase(@Param("name") String name);
}
