package com.example.quiz_World.repository;

import com.example.quiz_World.entities.wordSetEntity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordSetIdAndWordNumber(Long id, Long number);
}
