package com.example.quiz_world.repositoryTest.words;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.words.wordSet.WordSet;
import com.example.quiz_world.words.wordSetCategory.WordSetCategory;
import com.example.quiz_world.words.word.WordRepository;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryRepository;
import com.example.quiz_world.words.wordSet.WordSetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class WordSetRepositoryTest {
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;
    @Autowired
    WordSetRepository wordSetRepository;
    @Autowired
    WordRepository wordRepository;

    @BeforeEach
    public void setUp() {
        wordRepository.deleteAll();
        wordSetRepository.deleteAll();
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    public void findWordSetByUserId_test() {
        createWordSet("wordSet", 1L);

        List<WordSet> wordSetList = wordSetRepository.findByUserId(1L);

        assertEquals(wordSetList.get(0).getTitle(), "wordSet");
    }

    @Test
    public void findWordSetByStatus_test() {
        newWordSetWithStatus("public", Status.PUBLIC);
        newWordSetWithStatus("private", Status.PRIVATE);

        List<WordSet> wordSetList = wordSetRepository.findByStatus(Status.PUBLIC);

        assertEquals(wordSetList.get(0).getTitle(), "public");
    }

    @Test
    public void findWordSetByCategoryId_test() {
        WordSetCategory wordSetCategory = newWordSetCategory();
        newWordSetWithCategory("wordSet", wordSetCategory);

        List<WordSet> wordSetList = wordSetRepository.findByWordSetCategoryId(wordSetCategory.getId());

        assertEquals(wordSetList.get(0).getTitle(), "wordSet");
    }

    private void createWordSet(String title, Long userId) {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setTitle(title);
        wordSet.setUserId(userId);
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setStatus(Status.PUBLIC);
        wordSetRepository.save(wordSet);
    }

    private void newWordSetWithStatus(String title, Status status) {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setTitle(title);
        wordSet.setUserId(1L);
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setStatus(status);
        wordSetRepository.save(wordSet);
    }

    private void newWordSetWithCategory(String title, WordSetCategory wordSetCategory) {
        WordSet wordSet = new WordSet();
        wordSet.setTitle(title);
        wordSet.setUserId(1L);
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setStatus(Status.PUBLIC);
        wordSetRepository.save(wordSet);
    }

    private WordSetCategory newWordSetCategory() {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        return wordSetCategoryRepository.save(wordSetCategory);
    }
}

