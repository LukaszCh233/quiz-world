package com.example.quiz_World.repository;

import com.example.quiz_World.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByWordSetIdAndWordNumber(Long id, Long number);
}
