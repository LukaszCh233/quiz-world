package com.example.quiz_world.words.repository;

import com.example.quiz_world.user.entity.Status;
import com.example.quiz_world.words.entity.WordSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordSetRepository extends JpaRepository<WordSet, Long> {
    List<WordSet> findByStatus(Status status);

    List<WordSet> findByUserId(Long id);

    List<WordSet> findByWordSetCategoryId(Long id);
}
