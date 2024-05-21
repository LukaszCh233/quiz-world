package com.example.quiz_World.serviceTests;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.repository.*;
import com.example.quiz_World.service.QuizServiceImpl;
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
@ActiveProfiles("quizWorldTest")
public class QuizServiceTest {
    private final QuizRepository quizRepository;
    private final QuizServiceImpl quizService;
    private final UserRepository userRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public QuizServiceTest(QuizRepository quizRepository, QuizServiceImpl quizService, UserRepository userRepository,
                           QuizCategoryRepository quizCategoryRepository, QuestionRepository questionRepository,
                           ResultRepository resultRepository) {
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.userRepository = userRepository;
        this.quizCategoryRepository = quizCategoryRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void shouldCreateQuiz_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);

        //When
        QuizDTO createQuiz = quizService.createQuiz("testTitle", quizCategory, Status.PUBLIC, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(quizCategory.getName(), createQuiz.getCategory());
        assertEquals("testTitle", createQuiz.getTitle());
    }

    @Test
    void shouldAddQuestionToQuiz_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, null, null, new ArrayList<>(), null);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", null, new ArrayList<>());

        //When
        quizService.addQuestionsToQuiz(quiz.getId(), question);

        //Then
        Optional<Question> optionalQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), question.getQuestionNumber());
        assertTrue(optionalQuestion.isPresent());
        Question findQuestion = optionalQuestion.get();

        assertEquals(question.getContent(), findQuestion.getContent());
        assertEquals(question.getQuestionNumber(), findQuestion.getQuestionNumber());
    }

    @Test
    void shouldFindAllPublicQuizzes_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz(null, null, null, quizCategory, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, null, null, quizCategory, null, Status.PUBLIC);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<QuizDTO> quizDTOList = quizService.findAllPublicQuizzes();

        //Then
        assertEquals(2, quizDTOList.size());
    }

    @Test
    void shouldFindYourQuizzes_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz(null, user.getId(), null, quizCategory, null, Status.PUBLIC);
        Quiz quiz1 = new Quiz(null, user.getId(), null, quizCategory, null, Status.PUBLIC);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        List<QuizDTO> quizDTOList = quizService.findYourQuizzes(new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(2, quizDTOList.size());
        assertEquals(quiz.getTitle(), quizDTOList.get(0).getTitle());
        assertEquals(quiz1.getTitle(), quizDTOList.get(1).getTitle());
        assertEquals(quiz.getQuizCategory().getName(), quizDTOList.get(0).getCategory());
        assertEquals(quiz1.getQuizCategory().getName(), quizDTOList.get(1).getCategory());
    }

    @Test
    void shouldFindQuizById_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);
        Quiz quiz = new Quiz(null, null, "test", quizCategory, null, Status.PUBLIC);
        quizRepository.save(quiz);

        //When
        QuizDTO findQuiz = quizService.findQuizById(quiz.getId());

        //Then
        assertNotNull(findQuiz);
        assertEquals(quiz.getTitle(), findQuiz.getTitle());
    }

    @Test
    void shouldFindQuizByCategoryId_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);
        Quiz quiz = new Quiz(null, null, "test", quizCategory, null, Status.PUBLIC);
        quizRepository.save(quiz);

        //When
        List<QuizDTO> quizDTOList = quizService.findQuizByCategory(quizCategory.getId());

        //Then
        assertEquals(1, quizDTOList.size());
        assertFalse(quizDTOList.isEmpty());
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
        List<QuestionDTO> questionList = quizService.findQuestionsByQuizId(quiz.getId());

        //Then
        assertEquals(2, questionList.size());
    }

    @Test
    void shouldDeleteAllQuizzesForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), null, null, null, null);
        Quiz quiz1 = new Quiz(null, user.getId(), null, null, null, null);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        quizService.deleteAllQuizzesForUser(new TestPrincipal(user.getEmail()));

        //Then
        List<Quiz> quizList = quizRepository.findAll();
        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldDeleteQuizByIdForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), null, null, null, null);
        quizRepository.save(quiz);

        //When
        quizService.deleteQuizByIdForUser(quiz.getId(), new TestPrincipal(user.getEmail()));

        //Then
        List<Quiz> quizList = quizRepository.findAll();
        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldUpdateQuizByIdForUser_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz(null, user.getId(), "old", quizCategory, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Quiz updatedQuiz = new Quiz(null, user.getId(), "update", quizCategory, null, Status.PUBLIC);

        //When
        Quiz updateResult = quizService.updateQuizByIdForUser(quiz.getId(), updatedQuiz, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(quiz.getId(), updateResult.getId());
        assertEquals(updatedQuiz.getStatus(), updateResult.getStatus());
        assertEquals(updatedQuiz.getTitle(), updateResult.getTitle());
        assertEquals(updatedQuiz.getQuizCategory(), updateResult.getQuizCategory());
    }

    @Test
    void shouldUpdateQuizByIdForAdmin_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz(null, null, "old", quizCategory, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Quiz updatedQuiz = new Quiz(null, null, "update", quizCategory, null, Status.PUBLIC);

        //When
        Quiz updateResult = quizService.updateQuizByIdForAdmin(quiz.getId(), updatedQuiz);

        //Then
        assertEquals(quiz.getId(), updateResult.getId());
        assertEquals(updatedQuiz.getStatus(), updateResult.getStatus());
        assertEquals(updatedQuiz.getTitle(), updateResult.getTitle());
        assertEquals(updatedQuiz.getQuizCategory(), updateResult.getQuizCategory());
        assertEquals(updatedQuiz.getUserId(), updateResult.getUserId());
    }

    @Test
    void shouldDeleteAllQuizzesForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), null, null, null, null);
        Quiz quiz1 = new Quiz(null, user.getId(), null, null, null, null);
        quizRepository.save(quiz);
        quizRepository.save(quiz1);

        //When
        quizService.deleteAllQuizzesForAdmin();

        //Then
        List<Quiz> quizList = quizRepository.findAll();
        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldDeleteQuizByIdForAdmin_Test() {
        //Given
        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), null, null, null, null);
        quizRepository.save(quiz);

        //When
        quizService.deleteQuizByIdForAdmin(quiz.getId());

        //Then
        List<Quiz> quizList = quizRepository.findAll();
        assertTrue(quizList.isEmpty());
    }

    @Test
    void shouldFindQuizzesResults_Test() {
        //Given
        User user = new User(null, "user", "test1", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, null, "old", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Result result = new Result(null, user, quiz, null, 100D);
        Result result1 = new Result(null, user, quiz, null, 50D);
        resultRepository.save(result);
        resultRepository.save(result1);

        //When
        List<QuizResultDTO> quizzesResults = quizService.findQuizzesResults(new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(2, quizzesResults.size());
        assertFalse(quizzesResults.isEmpty());
        assertEquals(result.getScore(), quizzesResults.get(0).getScore());
        assertEquals(result1.getScore(), quizzesResults.get(1).getScore());
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
        quizService.deleteQuestionByNumberQuestionForUser(quiz.getId(), 1L, new TestPrincipal(user.getEmail()));

        //Then
        Optional<Question> findQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), 1L);
        assertFalse(findQuestion.isPresent());
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
        quizService.deleteQuestionByNumberQuestionForAdmin(quiz.getId(), 1L);

        //Then
        Optional<Question> findQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), 1L);
        assertFalse(findQuestion.isPresent());
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
        Question result = quizService.updateQuestionByQuestionNumberForUser(quiz.getId(), question.getQuestionNumber(), updateQuestion, new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(question.getId(), result.getId());
        assertEquals(updateQuestion.getContent(), result.getContent());
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
        Question result = quizService.updateQuestionByQuestionNumberForAdmin(quiz.getId(), question.getQuestionNumber(), updateQuestion);

        //Then
        assertEquals(question.getId(), result.getId());
        assertEquals(updateQuestion.getContent(), result.getContent());
    }

    @Test
    void shouldSolveQuiz_Test() {
        //Given
        List<UserAnswer> userAnswers = new ArrayList<>();
        userAnswers.add(new UserAnswer(1L));

        User user = new User(null, "user", "testEmail", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, user.getId(), "animal", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, new ArrayList<>());
        AnswerToQuiz answerToQuiz = new AnswerToQuiz(null, 1L, "a", true, question);
        AnswerToQuiz answerToQuiz1 = new AnswerToQuiz(null, 2L, "b", false, question);
        question.getAnswerToQuiz().add(answerToQuiz);
        question.getAnswerToQuiz().add(answerToQuiz1);

        questionRepository.save(question);

        //When
        double score = quizService.solveQuiz(quiz.getId(), userAnswers, new TestPrincipal(user.getEmail()));

        //Then
        Result result = resultRepository.findByUserIdAndQuizId(user.getId(), quiz.getId());
        assertEquals(score, result.getScore());
        assertEquals(100, score);
    }
}



