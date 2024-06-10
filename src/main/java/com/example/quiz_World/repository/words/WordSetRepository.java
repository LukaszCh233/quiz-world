package com.example.quiz_World.repository.words;

import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordSetRepository extends JpaRepository<WordSet, Long> {
    List<WordSet> findByStatus(Status status);

    List<WordSet> findByUserId(Long id);

    List<WordSet> findByWordSetCategoryId(Long id);
}
