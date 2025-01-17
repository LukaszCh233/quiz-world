package com.example.quiz_world.words.wordSetCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WordSetCategoryRepository extends JpaRepository<WordSetCategory, Long> {
    @Query("SELECT w FROM WordSetCategory w WHERE LOWER(w.name) = LOWER(:name)")
    Optional<WordSetCategory> findByNameIgnoreCase(@Param("name") String name);
}
