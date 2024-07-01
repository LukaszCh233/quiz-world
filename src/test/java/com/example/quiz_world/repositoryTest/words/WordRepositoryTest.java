package com.example.quiz_world.repositoryTest.words;

import com.example.quiz_world.user.entity.Status;
import com.example.quiz_world.words.entity.Word;
import com.example.quiz_world.words.entity.WordSet;
import com.example.quiz_world.words.entity.WordSetCategory;
import com.example.quiz_world.words.repository.WordRepository;
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
public class WordRepositoryTest {
    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;
    @Autowired
    WordSetRepository wordSetRepository;

    @BeforeEach
    public void setUp() {
        wordRepository.deleteAll();
        wordSetRepository.deleteAll();
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    public void findWordByWordSetIdAndWordNumber_test() {
        Word word = newWord("test", 1L);
        Long wordSetId = word.getWordSet().getId();

        Optional<Word> foundWord = wordRepository.findByWordSetIdAndWordNumber(wordSetId, 1L);

        assertTrue(foundWord.isPresent());
        assertEquals(foundWord.get().getWord(), "test");
    }

    private Word newWord(String wordName, Long wordNumber) {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setTitle("title");
        wordSet.setUserId(1L);
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setStatus(Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, wordNumber, wordName, "test", wordSet);

        return wordRepository.save(word);
    }
}
