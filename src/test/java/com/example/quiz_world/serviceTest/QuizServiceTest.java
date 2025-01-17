package com.example.quiz_world.serviceTest;

import com.example.quiz_world.quiz.question.AnswerToQuiz;
import com.example.quiz_world.quiz.quiz.*;
import com.example.quiz_world.quiz.reslult.QuizResultDTO;
import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.quizCategory.QuizCategory;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryRepository;
import com.example.quiz_world.quiz.question.QuizQuestionRepository;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryService;
import com.example.quiz_world.quiz.reslult.QuizResultService;
import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.quiz.reslult.ResultRepository;
import com.example.quiz_world.account.user.Role;
import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
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
public class QuizServiceTest {
    @Autowired
    QuizCategoryService quizCategoryService;
    @Autowired
    QuizCategoryRepository quizCategoryRepository;
    @Autowired
    QuizRepository quizRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuizQuestionRepository quizQuestionRepository;
    @Autowired
    ResultRepository resultRepository;
    @Autowired
    QuizService quizService;
    @Autowired
    QuizResultService quizResultService;

    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
        quizQuestionRepository.deleteAll();
        quizRepository.deleteAll();
        quizCategoryRepository.deleteAll();
    }

    @Test
    public void quizzesWithPublicStatusCanBeFindInGlobalQuizList_test() {
        newQuizWithStatus("quiz", Status.PUBLIC);
        newQuizWithStatus("secret", Status.PRIVATE);

        List<QuizDTO> publicQuizzes = quizService.findAllPublicQuizzes();

        Assertions.assertEquals(publicQuizzes.size(), 1);
        Assertions.assertEquals(publicQuizzes.get(0).title(), "quiz");
    }

    @Test
    public void userShouldFindOnlyOwnQuizzes_test() {
        newQuiz("mine", "user@example.com");
        newQuiz("someone", "other@example.com");

        List<QuizDTO> userQuizzes = quizService.findYourQuizzes(new TestPrincipal("user@example.com"));

        Assertions.assertEquals(userQuizzes.size(), 1);
        Assertions.assertEquals(userQuizzes.get(0).title(), "mine");
    }

    @Test
    public void userCanDeleteOnlyOwnQuizzes_test() {
        newQuiz("mine", "user@example.com");
        newQuiz("someone", "other@example.com");

        deleteOwnQuizzes();

        List<Quiz> quizList = quizRepository.findAll();

        Assertions.assertEquals(quizList.size(), 1);
        Assertions.assertEquals(quizList.get(0).getTitle(), "someone");
    }

    @Test
    public void userCantDeleteQuizThatIsNotHisOwn_test() {
        newQuiz("mine", "user@example.com");
        QuizDTO someoneQuiz = newQuiz("someone", "other@example.com");

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            deleteOwnQuiz(someoneQuiz.id());
        });

        List<Quiz> quizList = quizRepository.findAll();

        Assertions.assertEquals(quizList.size(), 2);
        Assertions.assertEquals(quizList.get(0).getTitle(), "mine");
        Assertions.assertEquals(quizList.get(1).getTitle(), "someone");
    }

    @Test
    public void adminCanDeleteAnyUsersQuiz_test() {
        newQuiz("mine", "user@example.com");
        newQuiz("someone", "other@example.com");

        quizService.deleteAllQuizzesForAdmin();

        List<Quiz> quizList = quizRepository.findAll();

        Assertions.assertTrue(quizList.isEmpty());
    }

    @Test
    public void whenSolveQuizCorrectResultShouldBeMax_test() {
        Quiz quiz = newQuizWithQuestions("mine", "user@example.com");

        List<UserAnswer> userAnswers = new ArrayList<>();
        UserAnswer answer1 = new UserAnswer(1L);
        UserAnswer answer2 = new UserAnswer(1L);
        userAnswers.add(answer1);
        userAnswers.add(answer2);

        double score = quizService.solveQuiz(quiz.getId(), userAnswers, new TestPrincipal("user@example.com"));

        Assertions.assertEquals(score, 100.0);
    }

    @Test
    public void userShouldFindOnlyOwnQuizResults_test() {
        newResult(50.0, "user@example.com");

        List<QuizResultDTO> quizResults = quizResultService.findYourQuizzesResults(new TestPrincipal("user@example.com"));

        Assertions.assertEquals(quizResults.size(), 1);
        Assertions.assertEquals(quizResults.get(0).score(), 50.0);
    }

    private void newResult(double score, String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);

        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle("title");
        quiz.setUserId(user.getId());
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quizRepository.save(quiz);

        Result result = new Result();
        result.setUser(user);
        result.setQuiz(quiz);
        result.setScore(score);

        resultRepository.save(result);
    }

    private void newQuizWithStatus(String title, Status status) {
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(null);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(status);
        quizRepository.save(quiz);
    }

    private Quiz newQuizWithQuestions(String title, String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(user.getId());
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quiz.setQuestions(new ArrayList<>());
        quiz = quizRepository.save(quiz);

        List<Question> questions = new ArrayList<>();

        questions.add(addQuestionsToQuiz(1L, "test", quiz));
        questions.add(addQuestionsToQuiz(2L, "test1", quiz));
        quiz.setQuestions(questions);

        return quizRepository.save(quiz);
    }

    private Question addQuestionsToQuiz(Long number, String content, Quiz quiz) {
        List<AnswerToQuiz> answerToQuizList = new ArrayList<>();
        AnswerToQuiz answerToQuiz1 = new AnswerToQuiz();
        answerToQuiz1.setAnswerNumber(1L);
        answerToQuiz1.setContent("a");
        answerToQuiz1.setCorrect(true);

        AnswerToQuiz answerToQuiz2 = new AnswerToQuiz();
        answerToQuiz2.setAnswerNumber(2L);
        answerToQuiz2.setContent("b");
        answerToQuiz2.setCorrect(false);

        answerToQuizList.add(answerToQuiz1);
        answerToQuizList.add(answerToQuiz2);

        Question question = new Question();
        question.setQuiz(quiz);
        question.setContent(content);
        question.setQuestionNumber(number);
        question.setAnswerToQuiz(answerToQuizList);

        answerToQuiz1.setQuestion(question);
        answerToQuiz2.setQuestion(question);

        return quizQuestionRepository.save(question);
    }

    private QuizCategory newQuizCategory(String name) {
        QuizCategory quizCategory = new QuizCategory(null, name);
        return quizCategoryRepository.save(quizCategory);
    }

    private void newQuizWithCategory(String title, QuizCategory quizCategory) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(1L);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);
        quizRepository.save(quiz);
    }

    private QuizDTO newQuiz(String title, String email) {
        User user = new User(null, "testName", email, "password", Role.USER);
        userRepository.save(user);
        QuizCategory quizCategory = new QuizCategory(null, "TestCategory");
        quizCategoryRepository.save(quizCategory);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUserId(null);
        quiz.setQuizCategory(quizCategory);
        quiz.setStatus(Status.PUBLIC);

        return quizService.createQuiz(quiz.getTitle(), quizCategory.getId(), quiz.getStatus(), new TestPrincipal(email
        ));
    }

    private void deleteOwnQuizzes() {
        quizService.deleteAllQuizzesForUser(new TestPrincipal("user@example.com"));
    }

    private void deleteOwnQuiz(Long id) {
        quizService.deleteQuizByIdForUser(id, new TestPrincipal("user@example.com"));
    }
}

