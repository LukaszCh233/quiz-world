package com.example.quiz_world.words.repository;

import com.example.quiz_world.words.entity.WordSetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordSetCategoryRepository extends JpaRepository<WordSetCategory, Long> {
}
