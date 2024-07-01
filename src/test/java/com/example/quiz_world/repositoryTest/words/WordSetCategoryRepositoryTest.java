package com.example.quiz_world.repositoryTest.words;

import com.example.quiz_world.words.entity.WordSetCategory;
import com.example.quiz_world.words.repository.WordSetCategoryRepository;
import com.example.quiz_world.words.repository.WordSetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class WordSetCategoryRepositoryTest {
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;
    @Autowired
    WordSetRepository wordSetRepository;

    @BeforeEach
    public void setUp() {
        wordSetRepository.deleteAll();
        wordSetCategoryRepository.deleteAll();

    }

    @Test
    public void findWordSetCategoryByNameIgnoreCase_test() {
        newCategory("TEST");

        Optional<WordSetCategory> foundWordSetCategory = wordSetCategoryRepository.findByNameIgnoreCase("test");

        assertTrue(foundWordSetCategory.isPresent());
        assertEquals(foundWordSetCategory.get().getName(), "TEST");
    }

    private void newCategory(String name) {
        WordSetCategory wordSetCategory = new WordSetCategory(null, name);
        wordSetCategoryRepository.save(wordSetCategory);
    }
}
