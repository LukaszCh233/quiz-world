package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.repository.QuizCategoryRepository;
import com.example.quiz_World.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class QuizRepositoryTest {
    @Autowired
    QuizCategoryRepository quizCategoryRepository;
    @Autowired
    QuizRepository quizRepository;

    @BeforeEach
    void setUp() {
        quizRepository.deleteAll();
    }

    @Test
    void shouldSaveQuiz_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", new QuizCategory(null, "test"), null, Status.PUBLIC);
        quizCategoryRepository.save(quiz.getQuizCategory());

        //When
        quizRepository.save(quiz);

        //Then
        Optional<Quiz> foundQuizOptional = quizRepository.findById(quiz.getId());
        assertTrue(foundQuizOptional.isPresent());
        Quiz foundQuiz = foundQuizOptional.get();

        assertEquals(foundQuiz.getId(), quiz.getId());
        assertEquals(foundQuiz.getStatus(), quiz.getStatus());
        assertEquals(foundQuiz.getTitle(), quiz.getTitle());
        assertEquals(foundQuiz.getQuizCategory(), quiz.getQuizCategory());
    }

    @Test
    void shouldFindQuizById_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", new QuizCategory(null, "test"), null, Status.PUBLIC);
        quizCategoryRepository.save(quiz.getQuizCategory());
        quizRepository.save(quiz);

        //When
        Optional<Quiz> foundQuizOptional = quizRepository.findById(quiz.getId());
        assertTrue(foundQuizOptional.isPresent());
        Quiz foundQuiz = foundQuizOptional.get();

        //Then
        assertEquals(foundQuiz.getId(), quiz.getId());
        assertEquals(foundQuiz.getStatus(), quiz.getStatus());
        assertEquals(foundQuiz.getTitle(), quiz.getTitle());
        assertEquals(foundQuiz.getQuizCategory(), quiz.getQuizCategory());
    }

    @Test
    void shouldFindAllQuizzes_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", null, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, null, "testTitle1", null, null, Status.PRIVATE);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<Quiz> quizList = quizRepository.findAll();

        //Then
        assertEquals(2, quizList.size());
        assertFalse(quizList.isEmpty());
        assertEquals(quizList.get(0).getTitle(), quiz.getTitle());
        assertEquals(quizList.get(1).getTitle(), quiz1.getTitle());
        assertEquals(quizList.get(0).getId(), quiz.getId());
        assertEquals(quizList.get(1).getId(), quiz1.getId());
    }

    @Test
    void shouldDeleteQuiz_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", null, null, Status.PUBLIC);
        quizRepository.save(quiz);

        //When
        quizRepository.delete(quiz);

        //Then
        List<Quiz> quizList = quizRepository.findAll();

        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldDeleteAllQuizzes_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", null, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, null, "testTitle1", null, null, Status.PRIVATE);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        quizRepository.deleteAll();

        //Then
        List<Quiz> quizList = quizRepository.findAll();
        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldFindQuizByStatus_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, "testTitle", null, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, null, "testTitle1", null, null, Status.PRIVATE);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<Quiz> quizList = quizRepository.findByStatus(quiz.getStatus());

        //Then
        assertEquals(1, quizList.size());
        assertNotNull(quizList);
        assertFalse(quizList.isEmpty());
    }

    @Test
    void shouldFindQuizByQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz(null, null, "testTitle", quizCategory, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, null, "testTitle", quizCategory, null, Status.PUBLIC);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<Quiz> quizList = quizRepository.findByQuizCategoryId(quizCategory.getId());

        //Then
        assertEquals(2, quizList.size());
        assertFalse(quizList.isEmpty());
        assertEquals(quizList.get(0).getId(),quiz.getId());
        assertEquals(quizList.get(0).getTitle(),quiz.getTitle());
        assertEquals(quizList.get(0).getQuizCategory(),quiz.getQuizCategory());
        assertEquals(quizList.get(1).getId(),quiz1.getId());
        assertEquals(quizList.get(1).getTitle(),quiz1.getTitle());
        assertEquals(quizList.get(1).getQuizCategory(),quiz1.getQuizCategory());
    }

    @Test
    void shouldFindQuizByUserId_Test() {
        //Given
        Long userId = 1L;

        Quiz quiz = new Quiz(null, userId, "testTitle", null, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, userId, "testTitle1", null, null, Status.PUBLIC);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<Quiz> quizList = quizRepository.findByUserId(userId);

        //Then
        assertFalse(quizList.isEmpty());
        assertEquals(2, quizList.size());
        assertEquals(quizList.get(0).getId(), quiz.getId());
        assertEquals(quizList.get(0).getUserId(), quiz.getUserId());
        assertEquals(quizList.get(0).getTitle(), quiz.getTitle());
        assertEquals(quizList.get(1).getId(), quiz1.getId());
        assertEquals(quizList.get(1).getUserId(), quiz1.getUserId());
        assertEquals(quizList.get(1).getTitle(), quiz1.getTitle());
    }
}
