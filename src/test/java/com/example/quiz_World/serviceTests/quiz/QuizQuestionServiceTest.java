package com.example.quiz_World.serviceTests.quiz;

import com.example.quiz_World.dto.QuestionDTO;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.AnswerToQuiz;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.repository.quiz.QuestionRepository;
import com.example.quiz_World.repository.quiz.QuizRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.service.quiz.QuizQuestionService;
import com.example.quiz_World.serviceTests.TestPrincipal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class QuizQuestionServiceTest {
    private final QuizRepository quizRepository;
    private final QuizQuestionService quizQuestionService;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuizQuestionServiceTest(QuizRepository quizRepository, QuizQuestionService quizQuestionService,
                                   UserRepository userRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.quizQuestionService = quizQuestionService;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void shouldAddQuestionToQuiz_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, null, null, new ArrayList<>(), null);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", null, new ArrayList<>());

        //When
        quizQuestionService.addQuestionsToQuiz(quiz.getId(), question);

        //Then
        Optional<Question> optionalQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), question.getQuestionNumber());
        assertTrue(optionalQuestion.isPresent());
        Question findQuestion = optionalQuestion.get();

        assertEquals(question.getContent(), findQuestion.getContent());
        assertEquals(question.getQuestionNumber(), findQuestion.getQuestionNumber());
    }

    @Test
    void shouldFindQuestionsByQuizId_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, null, null, new ArrayList<>(), Status.PUBLIC);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());
        Question question1 = new Question(null, 2L, "test1", quiz, new ArrayList<>());
        List<Question> questions = Arrays.asList(question, question1);
        questionRepository.saveAll(questions);

        quiz.setQuestions(questions);
        //When
        List<QuestionDTO> questionList = quizQuestionService.findQuestionsByQuizId(quiz.getId());

        //Then
        assertEquals(2, questionList.size());
    }

    @Transactional
    @Test
    void shouldDeleteQuestionByNumberQuestionForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail1", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), "old", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());

        AnswerToQuiz answerToQuiz = new AnswerToQuiz(null, 1L, "a", true, question);
        question.getAnswerToQuiz().add(answerToQuiz);

        questionRepository.save(question);

        //When
        quizQuestionService.deleteQuestionByNumberQuestionForUser(quiz.getId(), 1L, new TestPrincipal(user.getEmail()));

        //Then
        Optional<Question> findQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), 1L);
        assertFalse(findQuestion.isPresent());
    }

    @Test
    void shouldUpdateQuestionByQuestionNumberForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), "old", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());
        AnswerToQuiz answerToQuiz = new AnswerToQuiz(null, 1L, "a", true, question);
        question.getAnswerToQuiz().add(answerToQuiz);

        questionRepository.save(question);

        Question updateQuestion = new Question(null, 1L, "update", quiz, new ArrayList<>());
        updateQuestion.getAnswerToQuiz().add(answerToQuiz);

        //When
        Question result = quizQuestionService.updateQuestionByQuestionNumberForAdmin(quiz.getId(), question.getQuestionNumber(), updateQuestion);

        //Then
        assertEquals(question.getId(), result.getId());
        assertEquals(updateQuestion.getContent(), result.getContent());
    }

    @Test
    void shouldUpdateQuestionByQuestionNumberForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), "old", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());
        AnswerToQuiz answerToQuiz = new AnswerToQuiz(null, 1L, "a", true, question);
        question.getAnswerToQuiz().add(answerToQuiz);

        questionRepository.save(question);

        Question updateQuestion = new Question(null, 1L, "update", quiz, new ArrayList<>());
        updateQuestion.getAnswerToQuiz().add(answerToQuiz);

        //When
        Question result = quizQuestionService.updateQuestionByQuestionNumberForUser(quiz.getId(), question.getQuestionNumber(), updateQuestion, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(question.getId(), result.getId());
        assertEquals(updateQuestion.getContent(), result.getContent());
    }

    @Transactional
    @Test
    void shouldDeleteQuestionByNumberQuestionForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), "old", null, null, Status.PUBLIC);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());
        AnswerToQuiz answerToQuiz = new AnswerToQuiz(null, 1L, "a", true, question);
        question.getAnswerToQuiz().add(answerToQuiz);

        questionRepository.save(question);

        //When
        quizQuestionService.deleteQuestionByNumberQuestionForAdmin(quiz.getId(), 1L);

        //Then
        Optional<Question> findQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), 1L);
        assertFalse(findQuestion.isPresent());
    }

}
