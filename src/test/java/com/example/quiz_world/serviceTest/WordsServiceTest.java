package com.example.quiz_world.serviceTest;

import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.quiz.reslult.ResultRepository;
import com.example.quiz_world.account.user.Role;
import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import com.example.quiz_world.words.wordSet.WordSetDTO;
import com.example.quiz_world.words.wordSet.WordSetResultDTO;
import com.example.quiz_world.words.wordSet.AnswerToWordSet;
import com.example.quiz_world.words.word.Word;
import com.example.quiz_world.words.wordSet.WordSet;
import com.example.quiz_world.words.wordSetCategory.WordSetCategory;
import com.example.quiz_world.words.word.WordRepository;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryRepository;
import com.example.quiz_world.words.wordSet.WordSetRepository;
import com.example.quiz_world.words.wordSet.WordSetService;
import com.example.quiz_world.words.word.WordsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class WordsServiceTest {
    @Autowired
    WordSetRepository wordSetRepository;
    @Autowired
    WordSetCategoryRepository wordSetCategoryRepository;
    @Autowired
    WordSetService wordSetService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ResultRepository resultRepository;
    @Autowired
    WordRepository wordRepository;
    @Autowired
    WordsService wordsService;


    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        wordRepository.deleteAll();
        wordSetRepository.deleteAll();
        userRepository.deleteAll();
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    public void wordSetsWithPublicStatusCanBeFindInGlobalWordSetList_test() {
        newWordSetWithStatus("wordSet", Status.PUBLIC);
        newWordSetWithStatus("secret", Status.PRIVATE);

        List<WordSetDTO> publicWordSets = wordSetService.findPublicWordSets();

        Assertions.assertEquals(publicWordSets.size(), 1);
        Assertions.assertEquals(publicWordSets.get(0).title(), "wordSet");
    }

    @Test
    public void userShouldFindOnlyOwnWordSets_test() {
        newWordSet("mine", "user@example.com");
        newWordSet("someone", "other@example.com");

        List<WordSetDTO> userWordSets = wordSetService.findYourWordSets(new TestPrincipal("user@example.com"));

        Assertions.assertEquals(userWordSets.size(), 1);
        Assertions.assertEquals(userWordSets.get(0).title(), "mine");
    }

    @Test
    public void userCanDeleteOnlyOwnWordSets_test() {
        newWordSet("mine", "user@example.com");
        newWordSet("someone", "other@example.com");

        deleteOwnWordSets();

        List<WordSet> wordSetList = wordSetRepository.findAll();

        Assertions.assertEquals(wordSetList.size(), 1);
        Assertions.assertEquals(wordSetList.get(0).getTitle(), "someone");
    }

    @Test
    public void userCantDeleteWordSetThatIsNotHisOwn_test() {
        newWordSet("mine", "user@example.com");
        WordSetDTO someoneWordSet = newWordSet("someone", "other@example.com");

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            deleteOwnWordSet(someoneWordSet.id());
        });

        List<WordSet> wordSetList = wordSetRepository.findAll();

        Assertions.assertEquals(wordSetList.size(), 2);
        Assertions.assertEquals(wordSetList.get(0).getTitle(), "mine");
        Assertions.assertEquals(wordSetList.get(1).getTitle(), "someone");
    }

    @Test
    public void adminCanDeleteAnyUsersWordSet_test() {
        newWordSet("mine", "user@example.com");
        newWordSet("someone", "other@example.com");

        wordSetService.deleteAllWordSetsForAdmin();

        List<WordSet> wordSetList = wordSetRepository.findAll();

        Assertions.assertTrue(wordSetList.isEmpty());
    }

    @Test
    public void scoreShouldBeMaxWhenSolveWordSetCorrect() {
        WordSet wordSet = newWordSetWithWords("user@example.com");
        List<AnswerToWordSet> userAnswers = new ArrayList<>();
        AnswerToWordSet answerToWordSet1 = new AnswerToWordSet("translate1");
        AnswerToWordSet answerToWordSet2 = new AnswerToWordSet("translate2");
        userAnswers.add(answerToWordSet1);
        userAnswers.add(answerToWordSet2);

        double score = wordSetService.solveWordSet(wordSet.getId(), userAnswers, new TestPrincipal("user@example.com"));

        Assertions.assertEquals(score, 100.0);
    }

    @Test
    public void userShouldFindOnlyOwnQuizResults_test() {
        newResult(50.0, "user@example.com");

        List<WordSetResultDTO> wordSetResults = wordsService.findYourWordsResults(new TestPrincipal("user@example.com"));

        Assertions.assertEquals(wordSetResults.size(), 1);
        Assertions.assertEquals(wordSetResults.get(0).score(), 50.0);
    }

    private void newResult(double score, String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);

        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setTitle("title");
        wordSet.setUserId(1L);
        wordSet.setStatus(Status.PUBLIC);
        wordSetRepository.save(wordSet);

        Result result = new Result();
        result.setUser(user);
        result.setWordSet(wordSet);
        result.setScore(score);

        resultRepository.save(result);
    }

    private void newWordSetWithStatus(String title, Status status) {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "testCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setTitle(title);
        wordSet.setUserId(1L);
        wordSet.setStatus(status);
        wordSetRepository.save(wordSet);
    }

    private WordSetDTO newWordSet(String title, String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setTitle(title);
        wordSet.setUserId(1L);
        wordSet.setStatus(Status.PUBLIC);

        return wordSetService.createWordSet(wordSet.getTitle(), wordSetCategory.getId(), wordSet.getStatus(), new TestPrincipal(email
        ));
    }

    private WordSet newWordSetWithWords(String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);
        WordSetCategory wordSetCategory = new WordSetCategory(null, "TestCategory");
        wordSetCategoryRepository.save(wordSetCategory);

        WordSet wordSet = new WordSet();
        wordSet.setWordSetCategory(wordSetCategory);
        wordSet.setTitle("title");
        wordSet.setUserId(1L);
        wordSet.setWords(new ArrayList<>());
        wordSet.setStatus(Status.PUBLIC);
        wordSetRepository.save(wordSet);

        List<Word> wordList = new ArrayList<>();
        wordList.add(new Word(null, 1L, "word1", "translate1", wordSet));
        wordList.add(new Word(null, 2L, "word2", "translate2", wordSet));
        wordSet.setWords(wordList);

        return wordSetRepository.save(wordSet);
    }

    private void deleteOwnWordSets() {
        wordSetService.deleteAllWordSetsForUser(new TestPrincipal("user@example.com"));
    }

    private void deleteOwnWordSet(Long id) {
        wordSetService.deleteWordSetByIdForUser(id, new TestPrincipal("user@example.com"));
    }
}
