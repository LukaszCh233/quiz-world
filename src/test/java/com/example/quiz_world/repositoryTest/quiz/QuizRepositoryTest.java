package com.example.quiz_world.repositoryTest.quiz;

import com.example.quiz_world.quiz.entity.Quiz;
import com.example.quiz_world.quiz.entity.QuizCategory;
import com.example.quiz_world.quiz.repository.QuizCategoryRepository;
import com.example.quiz_world.quiz.repository.QuizRepository;
import com.example.quiz_world.user.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class QuizRepositoryTest {
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizCategoryRepository quizCategoryRepository;

    @BeforeEach
    public void setUp() {
        quizRepository.deleteAll();
        quizCategoryRepository.deleteAll();
    }

    @Test
    public void findQuizByUserId_test() {
        createQuiz("quiz", 1L);

        List<Quiz> quizList = quizRepository.findByUserId(1L);

        assertEquals(quizList.get(0).getTitle(), "quiz");
    }

    @Test
    public void findQuizByStatus_test() {
        newQuizWithStatus("public", Status.PUBLIC);
        newQuizWithStatus("private", Status.PRIVATE);

        List<Quiz> quizList = quizRepository.findByStatus(Status.PUBLIC);

        assertEquals(quizList.get(0).getTitle(), "public");
    }

    @Test
    public void findQuizByCategoryId_test() {
        QuizCategory quizCategory = newQuizCategory();
        newQuizWithCategory("quiz", quizCategory);

        List<Quiz> quizList = quizRepository.findByQuizCategoryId(quizCategory.getId());

        assertEquals(quizList.get(0).getTitle(), "quiz");
    }

    private void createQuiz(String title, Long userId) {
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(userId);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quizRepository.save(quiz);
    }

    private void newQuizWithStatus(String title, Status status) {
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(1L);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(status);
        quizRepository.save(quiz);
    }

    private void newQuizWithCategory(String title, QuizCategory quizCategory) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(1L);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quizRepository.save(quiz);
    }

    private QuizCategory newQuizCategory() {
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        return quizCategoryRepository.save(quizCategory);
    }
}