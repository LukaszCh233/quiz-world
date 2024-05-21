package com.example.quiz_World.serviceTests;

import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.wordSetEntity.*;
import com.example.quiz_World.repository.*;
import com.example.quiz_World.service.WordServiceImpl;
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
@ActiveProfiles("quizWorldTest")
public class WordServiceTest {
    private final WordServiceImpl wordService;
    private final WordSetRepository wordSetRepository;
    private final UserRepository userRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final ResultRepository resultRepository;
    private final WordRepository wordRepository;

    @Autowired
    public WordServiceTest(WordServiceImpl wordService, WordSetRepository wordSetRepository, UserRepository userRepository,
                           WordSetCategoryRepository wordSetCategoryRepository, ResultRepository resultRepository, WordRepository wordRepository) {
        this.wordService = wordService;
        this.wordSetRepository = wordSetRepository;
        this.userRepository = userRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.resultRepository = resultRepository;
        this.wordRepository = wordRepository;
    }

    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
        wordSetRepository.deleteAll();
    }

    @Test
    void shouldCreateWordSet_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        //When
        WordSetDTO createWordSet = wordService.createWordSet("testTitle", wordSetCategory, Status.PUBLIC, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(wordSetCategory.getName(), createWordSet.getCategory());
        assertEquals("testTitle", createWordSet.getTitle());
    }

    @Test
    void shouldFindAllPublicWordSets_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        WordSet wordSet1 = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        List<WordSetDTO> wordSetDTOList = wordService.findPublicWordSets();

        //Then
        assertEquals(2, wordSetDTOList.size());
    }

    @Test
    void shouldFindYourWordSets_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, user.getId(), wordSetCategory, Status.PUBLIC);
        WordSet wordSet1 = new WordSet(null, null, null, user.getId(), wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        List<WordSetDTO> wordSetDTOList = wordService.findYourWordSets(new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(2, wordSetDTOList.size());
        assertEquals(wordSet.getTitle(), wordSetDTOList.get(0).getTitle());
        assertEquals(wordSet1.getTitle(), wordSetDTOList.get(1).getTitle());
        assertEquals(wordSet.getWordSetCategory().getName(), wordSetDTOList.get(0).getCategory());
        assertEquals(wordSet1.getWordSetCategory().getName(), wordSetDTOList.get(1).getCategory());
    }

    @Test
    void shouldFindWordSetById_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        WordSetDTO findWordSet = wordService.findWordSetById(wordSet.getId());

        //Then
        assertNotNull(findWordSet);
        assertEquals(wordSet.getTitle(), findWordSet.getTitle());
    }

    @Test
    void shouldFindWordSetByCategoryId_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        List<WordSetDTO> wordSetDTOList = wordService.findWordSetByCategory(wordSetCategory.getId());

        //Then
        assertEquals(1, wordSetDTOList.size());
        assertFalse(wordSetDTOList.isEmpty());
    }

    @Test
    void shouldDeleteAllWordSetForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, null, null, user.getId(), null, null);
        WordSet wordSet1 = new WordSet(null, null, null, user.getId(), null, null);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        wordService.deleteAllWordSetsForUser(new TestPrincipal(user.getEmail()));

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
    }

    @Test
    void shouldDeleteWordSetByIdForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, null, null, user.getId(), null, null);
        wordSetRepository.save(wordSet);

        //When
        wordService.deleteWordSetByIdForUser(wordSet.getId(), new TestPrincipal(user.getEmail()));

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
    }

    @Test
    void shouldUpdateWordSetByIdForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), wordSetCategory, Status.PRIVATE);
        wordSetRepository.save(wordSet);
        WordSet updatedWordSet = new WordSet(null, "update", null, user.getId(), wordSetCategory, Status.PUBLIC);

        //When
        WordSet updateResult = wordService.updateWordSetByIdForUser(wordSet.getId(), updatedWordSet, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(wordSet.getId(), updateResult.getId());
        assertEquals(updatedWordSet.getStatus(), updateResult.getStatus());
        assertEquals(updatedWordSet.getTitle(), updateResult.getTitle());
        assertEquals(updatedWordSet.getWordSetCategory(), updateResult.getWordSetCategory());
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

    @Test
    void shouldUpdateWordSetByIdForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), wordSetCategory, Status.PRIVATE);
        wordSetRepository.save(wordSet);
        WordSet updatedWordSet = new WordSet(null, "update", null, user.getId(), wordSetCategory, Status.PUBLIC);

        //When
        WordSet updateResult = wordService.updateWordSetByIdForAdmin(wordSet.getId(), updatedWordSet);

        //Then
        assertEquals(wordSet.getId(), updateResult.getId());
        assertEquals(updatedWordSet.getStatus(), updateResult.getStatus());
        assertEquals(updatedWordSet.getTitle(), updateResult.getTitle());
        assertEquals(updatedWordSet.getWordSetCategory(), updateResult.getWordSetCategory());
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
    void shouldDeleteAllWordSetForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, null, null, user.getId(), null, null);
        WordSet wordSet1 = new WordSet(null, null, null, user.getId(), null, null);
        wordSetRepository.save(wordSet);
        wordSetRepository.save(wordSet1);

        //When
        wordService.deleteAllWordSetsForAdmin();

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
    }

    @Test
    void shouldDeleteWordSetByIdForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, null, null, user.getId(), null, null);
        wordSetRepository.save(wordSet);

        //When
        wordService.deleteWordSetByIdForAdmin(wordSet.getId());

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
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

    @Test
    void shouldSolveWordSet_Test() {
        List<AnswerToWordSet> userAnswers = new ArrayList<>();
        userAnswers.add(new AnswerToWordSet("test"));
        userAnswers.add(new AnswerToWordSet("test1"));

        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        WordSet wordSet = new WordSet(null, "test", null, user.getId(), null, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Word word = new Word(null, 1L, "test", "test", wordSet);
        Word word1 = new Word(null, 2L, "test1", "test1", wordSet);
        wordRepository.save(word);
        wordRepository.save(word1);

        //When
        double score = wordService.solveWordSet(wordSet.getId(), userAnswers, new TestPrincipal(user.getEmail()));

        //Then
        Result result = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSet.getId());
        assertEquals(score, result.getScore());
        assertEquals(100, score);
    }
}