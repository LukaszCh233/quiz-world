package com.example.quiz_world.serviceTests.words;

import com.example.quiz_world.dto.WordSetDTO;
import com.example.quiz_world.entities.Result;
import com.example.quiz_world.entities.Role;
import com.example.quiz_world.entities.Status;
import com.example.quiz_world.entities.User;
import com.example.quiz_world.entities.wordSetEntity.AnswerToWordSet;
import com.example.quiz_world.entities.wordSetEntity.Word;
import com.example.quiz_world.entities.wordSetEntity.WordSet;
import com.example.quiz_world.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_world.repository.ResultRepository;
import com.example.quiz_world.repository.UserRepository;
import com.example.quiz_world.repository.words.WordRepository;
import com.example.quiz_world.repository.words.WordSetCategoryRepository;
import com.example.quiz_world.repository.words.WordSetRepository;
import com.example.quiz_world.service.words.WordSetService;
import com.example.quiz_world.serviceTests.TestPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WordSetServiceTest {
    private final WordSetRepository wordSetRepository;
    private final UserRepository userRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final ResultRepository resultRepository;
    private final WordRepository wordRepository;
    private final WordSetService wordSetService;

    @Autowired
    public WordSetServiceTest(WordSetRepository wordSetRepository, UserRepository userRepository,
                              WordSetCategoryRepository wordSetCategoryRepository, ResultRepository resultRepository,
                              WordRepository wordRepository, WordSetService wordSetService) {
        this.wordSetRepository = wordSetRepository;
        this.userRepository = userRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.resultRepository = resultRepository;
        this.wordRepository = wordRepository;
        this.wordSetService = wordSetService;
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
        WordSetDTO createWordSet = wordSetService.createWordSet("testTitle", wordSetCategory.getId(), Status.PUBLIC, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(wordSetCategory.getName(), createWordSet.category());
        assertEquals("testTitle", createWordSet.title());
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
        List<WordSetDTO> wordSetDTOList = wordSetService.findPublicWordSets();

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
        List<WordSetDTO> wordSetDTOList = wordSetService.findYourWordSets(new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(2, wordSetDTOList.size());
        assertEquals(wordSet.getTitle(), wordSetDTOList.get(0).title());
        assertEquals(wordSet1.getTitle(), wordSetDTOList.get(1).title());
        assertEquals(wordSet.getWordSetCategory().getName(), wordSetDTOList.get(0).category());
        assertEquals(wordSet1.getWordSetCategory().getName(), wordSetDTOList.get(1).category());
    }

    @Test
    void shouldFindWordSetById_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        WordSetDTO findWordSet = wordSetService.findWordSetById(wordSet.getId());

        //Then
        assertNotNull(findWordSet);
        assertEquals(wordSet.getTitle(), findWordSet.title());
    }

    @Test
    void shouldFindWordSetByCategoryId_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet(null, null, null, null, wordSetCategory, Status.PUBLIC);
        wordSetRepository.save(wordSet);

        //When
        List<WordSetDTO> wordSetDTOList = wordSetService.findWordSetByCategory(wordSetCategory.getId());

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
        wordSetService.deleteAllWordSetsForUser(new TestPrincipal(user.getEmail()));

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
        wordSetService.deleteWordSetByIdForUser(wordSet.getId(), new TestPrincipal(user.getEmail()));

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
        WordSet updateResult = wordSetService.updateWordSetByIdForUser(wordSet.getId(), updatedWordSet, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(wordSet.getId(), updateResult.getId());
        assertEquals(updatedWordSet.getStatus(), updateResult.getStatus());
        assertEquals(updatedWordSet.getTitle(), updateResult.getTitle());
        assertEquals(updatedWordSet.getWordSetCategory(), updateResult.getWordSetCategory());
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
        WordSet updateResult = wordSetService.updateWordSetByIdForAdmin(wordSet.getId(), updatedWordSet);

        //Then
        assertEquals(wordSet.getId(), updateResult.getId());
        assertEquals(updatedWordSet.getStatus(), updateResult.getStatus());
        assertEquals(updatedWordSet.getTitle(), updateResult.getTitle());
        assertEquals(updatedWordSet.getWordSetCategory(), updateResult.getWordSetCategory());
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
        wordSetService.deleteAllWordSetsForAdmin();

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
        wordSetService.deleteWordSetByIdForAdmin(wordSet.getId());

        //Then
        List<WordSet> wordSetList = wordSetRepository.findAll();
        assertTrue(wordSetList.isEmpty());
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
        double score = wordSetService.solveWordSet(wordSet.getId(), userAnswers, new TestPrincipal(user.getEmail()));

        //Then
        Result result = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSet.getId());
        assertEquals(score, result.getScore());
    }
}