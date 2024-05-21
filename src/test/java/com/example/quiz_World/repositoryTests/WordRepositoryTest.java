package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.WordRepository;
import com.example.quiz_World.repository.WordSetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("testQuizWorld")
public class WordRepositoryTest {
    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordSetRepository wordSetRepository;
    @Autowired
    ResultRepository resultRepository;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        wordRepository.deleteAll();
        wordSetRepository.deleteAll();
    }

    @Test
    void shouldSaveWord_Test() {
        //Given
        Word word = new Word(null, null, "test", "test", null);

        //When
        wordRepository.save(word);

        //Then
        List<Word> wordList = wordRepository.findAll();

        assertFalse(wordList.isEmpty());
        assertEquals(1, wordList.size());
        assertEquals(wordList.get(0).getId(), word.getId());
        assertEquals(wordList.get(0).getWordNumber(), word.getWordNumber());
        assertEquals(wordList.get(0).getWord(), word.getWord());
        assertEquals(wordList.get(0).getTranslation(), word.getTranslation());
    }

    @Test
    void shouldDeleteWord_Test() {
        //Given
        Word word = new Word();
        wordRepository.save(word);

        //When
        wordRepository.delete(word);

        //Then
        List<Word> wordList = wordRepository.findAll();

        assertTrue(wordList.isEmpty());
    }

    @Test
    void shouldDeleteAllWords_Test() {
        //Given
        Word word = new Word(null, null, null, null, null);
        Word word1 = new Word(null, null, null, null, null);
        wordRepository.save(word);
        wordRepository.save(word1);

        //When
        wordRepository.deleteAll();

        //Then
        List<Word> wordList = wordRepository.findAll();
        assertTrue(wordList.isEmpty());
    }

    @Test
    void shouldFindWordByWordSetIdAndWordNumber_Test() {
        //Given
        WordSet wordSet = new WordSet(null, null, null, null, null, null);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, "test", "test", wordSet);
        wordRepository.save(word);

        //When
        Optional<Word> optionalWord = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), word.getWordNumber());
        assertTrue(optionalWord.isPresent());
        Word findWord = optionalWord.get();

        //Then
        assertEquals(findWord.getId(), word.getId());
        assertEquals(findWord.getWord(), word.getWord());
        assertEquals(findWord.getTranslation(), word.getTranslation());
    }
}
