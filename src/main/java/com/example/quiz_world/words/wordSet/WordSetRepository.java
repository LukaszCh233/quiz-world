package com.example.quiz_world.words.wordSet;

import com.example.quiz_world.account.user.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordSetRepository extends JpaRepository<WordSet, Long> {
    List<WordSet> findByStatus(Status status);

    List<WordSet> findByUserId(Long id);

    List<WordSet> findByWordSetCategoryId(Long id);

    boolean existsByWordSetCategoryId(Long id);

    Optional<WordSet> findFirstBy();

    void deleteByUserId(Long userId);

    List<WordSet> findByWordSetCategoryIdAndStatus(Long categoryId, Status status);

    Optional<WordSet> findByIdAndStatus(Long id, Status status);
}
