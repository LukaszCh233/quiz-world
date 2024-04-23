package com.example.quiz_World.repository;

import com.example.quiz_World.entities.CategoryQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQuizRepository extends JpaRepository<CategoryQuiz, Long> {
    CategoryQuiz findCategoryByName(String name);
}
