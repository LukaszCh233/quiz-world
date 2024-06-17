package com.example.quiz_world.repository.words;

import com.example.quiz_world.entities.wordSetEntity.WordSetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordSetCategoryRepository extends JpaRepository<WordSetCategory, Long> {
}
