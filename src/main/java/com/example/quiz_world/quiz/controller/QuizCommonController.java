package com.example.quiz_world.quiz.controller;

import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.question.QuestionDTO;
import com.example.quiz_world.quiz.question.QuizQuestionService;
import com.example.quiz_world.quiz.quiz.Quiz;
import com.example.quiz_world.quiz.quiz.QuizDTO;
import com.example.quiz_world.quiz.quiz.QuizService;
import com.example.quiz_world.quiz.quiz.UserAnswer;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryDTO;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryService;
import com.example.quiz_world.quiz.reslult.QuizResultDTO;
import com.example.quiz_world.quiz.reslult.QuizResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/common")
public class QuizCommonController {
    private final QuizService quizService;
    private final QuizCategoryService quizCategoryService;
    private final QuizQuestionService quizQuestionService;
    private final QuizResultService quizResultService;

    public QuizCommonController(QuizService quizService, QuizCategoryService quizCategoryService,
                                QuizQuestionService quizQuestionService, QuizResultService quizResultService) {
        this.quizService = quizService;
        this.quizCategoryService = quizCategoryService;
        this.quizQuestionService = quizQuestionService;
        this.quizResultService = quizResultService;
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizDTO>> displayQuizzes() {
        List<QuizDTO> quizzes = quizService.findAllPublicQuizzes();

        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<QuizDTO> displayQuiz(@PathVariable Long id) {
        QuizDTO quizDTO = quizService.findQuizById(id);

        return ResponseEntity.ok(quizDTO);
    }

    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<List<QuestionDTO>> displayQuestions(@PathVariable Long quizId) {
        List<QuestionDTO> questions = quizQuestionService.findQuestionsByQuizId(quizId);

        return ResponseEntity.ok(questions);
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody Quiz quiz, Principal principal) {
        QuizDTO createQuiz = quizService.createQuiz(quiz.getTitle(), quiz.getQuizCategory().getId(), quiz.getStatus(), principal);

        return ResponseEntity.ok(createQuiz);
    }

    @PostMapping("/quiz-solve/{quizId}")
    public ResponseEntity<String> solveQuiz(@PathVariable Long quizId, @RequestBody List<UserAnswer> userAnswersToQuiz, Principal principal) {
        double score = quizService.solveQuiz(quizId, userAnswersToQuiz, principal);

        return ResponseEntity.ok("Quiz solved successfully. Your score: " + score);
    }

    @PostMapping("/quiz/{quizId}/question")
    public ResponseEntity<String> addQuestionToQuiz(@PathVariable Long quizId, @Valid @RequestBody Question question) {
        quizQuestionService.addQuestionsToQuiz(quizId, question);

        return ResponseEntity.ok("Question added to quiz successfully");
    }

    @GetMapping("/user-quizzes")
    public ResponseEntity<List<QuizDTO>> displayYourQuizzes(Principal principal) {
        List<QuizDTO> quizzes = quizService.findYourQuizzes(principal);

        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/quizzes/category/{categoryId}")
    public ResponseEntity<List<QuizDTO>> displayQuizzesByCategory(@PathVariable Long categoryId) {
        List<QuizDTO> quizDTOList = quizService.findQuizByCategory(categoryId);

        return ResponseEntity.ok(quizDTOList);
    }

    @GetMapping("/quizCategories")
    public ResponseEntity<List<QuizCategoryDTO>> displayQuizCategories() {
        List<QuizCategoryDTO> quizCategoryDTOList = quizCategoryService.findAllQuizCategories();

        return ResponseEntity.ok(quizCategoryDTOList);
    }

    @GetMapping("/quiz/score")
    public ResponseEntity<List<QuizResultDTO>> displayYourQuizzesScore(Principal principal) {
        List<QuizResultDTO> quizResultDTOS = quizResultService.findYourQuizzesResults(principal);

        return ResponseEntity.ok(quizResultDTOS);
    }

    @GetMapping("/quiz/globalScore")
    public ResponseEntity<List<QuizResultDTO>> displayQuizzesScore() {
        List<QuizResultDTO> quizResultDTOS = quizResultService.findQuizzesResults();

        return ResponseEntity.ok(quizResultDTOS);
    }
}
