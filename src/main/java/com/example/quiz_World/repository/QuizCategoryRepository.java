package com.example.quiz_World.repository;

import com.example.quiz_World.entities.quizEntity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Long> {
    Optional<QuizCategory> findByName(String name);

    @Query("SELECT q FROM QuizCategory q WHERE LOWER(q.name) = LOWER(:name)")
    Optional<QuizCategory> findByNameIgnoreCase(@Param("name") String name);
}
