package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.repository.QuizRepository;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.repository.WordSetRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class ResultRepositoryTest {
    @Autowired
    ResultRepository resultRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    WordSetRepository wordSetRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveResult_Test() {
        //Given
        Result result = new Result(null, null, null, null, 100.0);

        //When
        resultRepository.save(result);

        //Then
        List<Result> resultList = resultRepository.findAll();

        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
    }

    @Transactional
    @Test
    void shouldDeleteResultByQuiz_Test() {
        //Given
        Quiz quiz = new Quiz();
        quizRepository.save(quiz);

        Result result = new Result(null, null, quiz, null, 100.0);
        resultRepository.save(result);

        //When
        resultRepository.deleteByQuiz(quiz);

        //Then
        List<Result> resultList = resultRepository.findAll();

        assertTrue(resultList.isEmpty());
    }

    @Transactional
    @Test
    void shouldDeleteResultByWordSet_Test() {
        //Given
        WordSet wordSet = new WordSet();
        wordSetRepository.save(wordSet);

        Result result = new Result(null, null, null, wordSet, 100.0);
        resultRepository.save(result);

        //When
        resultRepository.deleteByWordSet(wordSet);

        //Then
        List<Result> resultList = resultRepository.findAll();

        assertTrue(resultList.isEmpty());
    }

    @Test
    void shouldFindResultByUserIdAndQuizId_Test() {
        //Given
        User user = new User();
        userRepository.save(user);

        Quiz quiz = new Quiz();
        quizRepository.save(quiz);

        Result result = new Result(null, user, quiz, null, 100.0);
        resultRepository.save(result);

        //When
        Result findResult = resultRepository.findByUserIdAndQuizId(user.getId(), quiz.getId());

        //Then
        assertEquals(findResult.getUser(), result.getUser());
        assertEquals(findResult.getId(), result.getId());
        assertEquals(findResult.getScore(), result.getScore());
    }

    @Test
    void shouldFindResultByUserIdAndWordSet_Test() {
        //Given
        User user = new User();
        userRepository.save(user);

        WordSet wordSet = new WordSet();
        wordSetRepository.save(wordSet);

        Result result = new Result(null, user, null, wordSet, 100.0);
        resultRepository.save(result);

        //When
        Result findResult = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSet.getId());

        //Then
        assertEquals(findResult.getUser(), result.getUser());
        assertEquals(findResult.getId(), result.getId());
        assertEquals(findResult.getScore(), result.getScore());
    }

    @Test
    void shouldFindResultByUserId_Test() {
        //Given
        User user = new User();
        userRepository.save(user);

        Result result = new Result(null, user, null, null, 100.0);
        resultRepository.save(result);

        //When
        List<Result> resultList = resultRepository.findByUserId(user.getId());

        //Then
        assertEquals(1, resultList.size());
        assertFalse(resultList.isEmpty());
    }
}

