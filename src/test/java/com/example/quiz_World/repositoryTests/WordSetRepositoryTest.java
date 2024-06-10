package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.words.WordSetCategoryRepository;
import com.example.quiz_World.repository.words.WordSetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WordSetRepositoryTest {
    @Autowired
    WordSetRepository wordSetRepository;
    @Autowired
    ResultRepository resultRepository;
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        wordSetRepository.deleteAll();
    }

    @Test
    void shouldSaveWordSet_Test() {
        //Given
        WordSet wordSet = new WordSet();

        //When
        wordSetRepository.save(wordSet);

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();

        assertFalse(wordSetList.isEmpty());
        assertEquals(1, wordSetList.size());
    }

    @Test
    void shouldFindWordSetById_Test() {
        //Given
        WordSet wordSet = new WordSet(null, "test", null, null, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        Optional<WordSet> optionalWordSet = wordSetRepository.findById(wordSet.getId());
        assertTrue(optionalWordSet.isPresent());
        WordSet findWordSet = optionalWordSet.get();

        //Then
        assertEquals(findWordSet.getTitle(), wordSet.getTitle());
        assertEquals(findWordSet.getId(), wordSet.getId());
        assertEquals(findWordSet.getStatus(), wordSet.getStatus());
    }

    @Test
    void shouldFindWordSetByStatus_Test() {
        //Given
        WordSet wordSet = new WordSet();
        wordSetRepository.save(wordSet);

        //When
        List<WordSet> wordSetList = wordSetRepository.findByStatus(wordSet.getStatus());

        //Then
        assertFalse(wordSetList.isEmpty());
        assertEquals(1, wordSetList.size());
    }

    @Test
    void shouldFindWordSetByUserId_Test() {
        //Given
        Long userId = 1L;

        WordSet wordSet = new WordSet(null, null, null, userId, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        List<WordSet> wordSetList = wordSetRepository.findByUserId(userId);

        //Then
        assertFalse(wordSetList.isEmpty());
        assertEquals(1, wordSetList.size());
    }

    @Test
    void shouldFindWordSetByCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        WordSet wordSet1 = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        List<WordSet> wordSetList = wordSetRepository.findByWordSetCategoryId(wordSetCategory.getId());

        //Then
        assertEquals(2, wordSetList.size());
        assertFalse(wordSetList.isEmpty());
    }

    @Test
    void shouldDeleteAllWordSets_Test() {
        WordSet wordSet = new WordSet(null, null, null, null, null, Status.PUBLIC);
        WordSet wordSet1 = new WordSet(null, null, null, null, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        wordSetRepository.deleteAll();

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
    }

    @Test
    void shouldDeleteWordSet_Test() {
        //Given
        WordSet wordSet = new WordSet();
        wordSetRepository.save(wordSet);

        //When
        wordSetRepository.delete(wordSet);

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
    }

    @Test
    void shouldFindAllWordSets_Test() {
        WordSet wordSet = new WordSet(null, null, null, null, null, Status.PUBLIC);
        WordSet wordSet1 = new WordSet(null, null, null, null, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        List<WordSet> wordSetList = wordSetRepository.findAll();

        //Then
        assertFalse(wordSetList.isEmpty());
        assertEquals(2, wordSetList.size());
    }
}
