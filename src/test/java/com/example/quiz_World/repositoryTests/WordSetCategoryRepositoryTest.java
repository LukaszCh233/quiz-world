package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.repository.WordSetCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class WordSetCategoryRepositoryTest {
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;

    @BeforeEach
    void setUp() {
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    void shouldSaveWordSetCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");

        //When
        wordSetCategoryRepository.save(wordSetCategory);

        //Then
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();

        assertFalse(wordSetCategoryList.isEmpty());
        assertEquals(1, wordSetCategoryList.size());
    }

    @Test
    void shouldFindAllWordSetCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        WordSetCategory wordSetCategory1 = new WordSetCategory(null, "testCategory1");

        wordSetCategoryRepository.save(wordSetCategory);
        wordSetCategoryRepository.save(wordSetCategory1);

        //When
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();

        //Then
        assertEquals(2, wordSetCategoryList.size());
        assertTrue(wordSetCategoryList.contains(wordSetCategory));
        assertTrue(wordSetCategoryList.contains(wordSetCategory1));
    }

    @Test
    void shouldFindWordSetCategoryById_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");

        wordSetCategoryRepository.save(wordSetCategory);

        //When
        Optional<WordSetCategory> foundCategory = wordSetCategoryRepository.findById(wordSetCategory.getId());

        //Then
        assertTrue(foundCategory.isPresent());
        assertEquals(foundCategory.get().getName(), wordSetCategory.getName());
    }

    @Test
    void shouldDeleteWordSetCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");

        wordSetCategoryRepository.save(wordSetCategory);

        //When
        wordSetCategoryRepository.delete(wordSetCategory);

        //Then
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();

        assertTrue(wordSetCategoryList.isEmpty());
    }
}


