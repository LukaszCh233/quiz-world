package com.example.quiz_World.serviceTests.words;

import com.example.quiz_World.dto.WordDTO;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.repository.words.WordRepository;
import com.example.quiz_World.repository.words.WordSetRepository;
import com.example.quiz_World.service.words.WordsService;
import com.example.quiz_World.serviceTests.TestPrincipal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WordsServiceTest {
    private final WordsService wordService;
    private final WordSetRepository wordSetRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    @Autowired
    public WordsServiceTest(WordsService wordService, WordSetRepository wordSetRepository, UserRepository userRepository,
                            WordRepository wordRepository) {
        this.wordService = wordService;
        this.wordSetRepository = wordSetRepository;
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
    }

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        wordSetRepository.deleteAll();
    }

    @Transactional
    @Test
    void shouldDeleteWordByNumberWordForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), null, Status.PRIVATE);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, null, null, wordSet);
        wordRepository.save(word);

        //When
        wordService.deleteWordByNumberWordSetForUser(wordSet.getId(), 1L, new TestPrincipal(user.getEmail()));

        //Then
        Optional<Word> findWord = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), 1L);
        assertFalse(findWord.isPresent());
    }

    @Transactional
    @Test
    void shouldDeleteWordByNumberWordForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), null, Status.PRIVATE);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, null, null, wordSet);
        wordRepository.save(word);

        //When
        wordService.deleteWordByNumberWordSetForAdmin(wordSet.getId(), 1L);

        //Then
        Optional<Word> findWord = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), 1L);
        assertFalse(findWord.isPresent());
    }

    @Test
    void shouldFindWordsByWordSet_Test() {
        //Given
        WordSet wordSet = new WordSet(null, "test", null, null, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, null, null, wordSet);
        wordRepository.save(word);

        //When
        List<WordDTO> wordList = wordService.findWordsByWordSetId(wordSet.getId());

        //Then
        assertEquals(1, wordList.size());
        assertFalse(wordList.isEmpty());
    }

    @Test
    void shouldUpdateWordForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, "test", "test", wordSet);
        wordRepository.save(word);

        Word updateWord = new Word(null, 1L, "update", "update", wordSet);

        //When
        Word result = wordService.updateWordForUser(wordSet.getId(), updateWord.getWordNumber(), updateWord, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(word.getId(), result.getId());
        assertEquals(updateWord.getWord(), result.getWord());
        assertEquals(updateWord.getTranslation(), result.getTranslation());
    }

    @Test
    void shouldUpdateWordForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, "test", "test", wordSet);
        wordRepository.save(word);

        Word updateWord = new Word(null, 1L, "update", "update", wordSet);

        //When
        Word result = wordService.updateWordForAdmin(wordSet.getId(), updateWord.getWordNumber(), updateWord);

        //Then
        assertEquals(word.getId(), result.getId());
        assertEquals(updateWord.getWord(), result.getWord());
        assertEquals(updateWord.getTranslation(), result.getTranslation());
    }

    @Test
    void shouldAddWordToWordSet_Test() {
        WordSet wordSet = new WordSet(null, null, new ArrayList<>(), null, null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, "test", "test", null);

        //When
        wordService.addWordToWordSet(wordSet.getId(), word);

        //Then
        Optional<Word> optionalWord = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), word.getWordNumber());
        assertTrue(optionalWord.isPresent());
        Word findWord = optionalWord.get();
        assertEquals(word.getWord(), findWord.getWord());
        assertEquals(word.getTranslation(), findWord.getTranslation());
        assertEquals(word.getWordNumber(), findWord.getWordNumber());
    }
}
