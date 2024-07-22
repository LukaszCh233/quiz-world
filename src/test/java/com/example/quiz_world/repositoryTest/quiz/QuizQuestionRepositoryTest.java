package com.example.quiz_world.repositoryTest.quiz;

import com.example.quiz_world.quiz.entity.AnswerToQuiz;
import com.example.quiz_world.quiz.entity.Question;
import com.example.quiz_world.quiz.entity.Quiz;
import com.example.quiz_world.quiz.entity.QuizCategory;
import com.example.quiz_world.quiz.repository.QuizCategoryRepository;
import com.example.quiz_world.quiz.repository.QuizQuestionRepository;
import com.example.quiz_world.quiz.repository.QuizRepository;
import com.example.quiz_world.user.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class QuizQuestionRepositoryTest {
    @Autowired
    QuizQuestionRepository quizQuestionRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    QuizCategoryRepository quizCategoryRepository;
    @BeforeEach
    public void setUp() {
        quizRepository.deleteAll();
        quizCategoryRepository.deleteAll();
        quizQuestionRepository.deleteAll();
    }

    @Test
    public void findQuestionByQuizIdAndQuestionNumber_test() {
        Question question = newQuestion("test",1L);
        Long quizId = question.getQuiz().getId();

        Optional<Question> foundQuestion = quizQuestionRepository.findByQuizIdAndQuestionNumber(quizId, question.getQuestionNumber());

        assertTrue(foundQuestion.isPresent());
        assertEquals(foundQuestion.get().getContent(), "test");
    }

    private Question newQuestion(String content,Long questionNumber) {
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle("title");
        quiz.setUserId(1L);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quizRepository.save(quiz);

        List<AnswerToQuiz> answerToQuiz = new ArrayList<>();
        Question quizQuestion = new Question(null, questionNumber, content, quiz, answerToQuiz);

        return quizQuestionRepository.save(quizQuestion);
    }
}