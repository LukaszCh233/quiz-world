package com.example.quiz_World.repository;

import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSetCategoryRepository extends JpaRepository<WordSetCategory, Long> {
}
