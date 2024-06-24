package com.example.quiz_world.quiz.controller;

import com.example.quiz_world.quiz.entity.Question;
import com.example.quiz_world.quiz.entity.Quiz;
import com.example.quiz_world.quiz.entity.QuizCategory;
import com.example.quiz_world.quiz.service.QuizCategoryService;
import com.example.quiz_world.quiz.service.QuizQuestionService;
import com.example.quiz_world.quiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class QuizAdminController {
    private final QuizService quizService;
    private final QuizCategoryService quizCategoryService;
    private final QuizQuestionService quizQuestionService;

    public QuizAdminController(QuizService quizService, QuizQuestionService quizQuestionService,
                               QuizCategoryService quizCategoryService) {
        this.quizService = quizService;
        this.quizQuestionService = quizQuestionService;
        this.quizCategoryService = quizCategoryService;
    }

    @PutMapping("/quiz-category/{categoryId}")
    public ResponseEntity<?> updateQuizCategory(@PathVariable Long categoryId, @Valid @RequestBody QuizCategory quizCategory) {
        QuizCategory updateCategory = quizCategoryService.updateQuizCategory(categoryId, quizCategory);
        return ResponseEntity.ok(updateCategory);
    }

    @DeleteMapping("/quizzes")
    public ResponseEntity<?> deleteAllQuizzes() {
        quizService.deleteAllQuizzesForAdmin();
        return ResponseEntity.ok("All Quizzes have been deleted");
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuizByIdForAdmin(quizId);
        return ResponseEntity.ok("Quiz have been deleted");
    }

    @PutMapping("/quiz/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable Long quizId, @Valid @RequestBody Quiz quiz) {
        quizService.updateQuizByIdForAdmin(quizId, quiz);

        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<?> updateQuizQuestions(@PathVariable Long quizId, @PathVariable Long questionNumber, @Valid @RequestBody Question question) {
        quizQuestionService.updateQuestionByQuestionNumberForAdmin(quizId, questionNumber, question);

        return ResponseEntity.ok("Question updated");
    }

    @PostMapping("/quiz-category")
    public ResponseEntity<QuizCategory> createQuizCategory(@Valid @RequestBody QuizCategory quizCategory) {
        QuizCategory createQuizCategory = quizCategoryService.createQuizCategory(quizCategory);

        return ResponseEntity.ok(createQuizCategory);
    }

    @DeleteMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber) {
        quizQuestionService.deleteQuestionByNumberQuestionForAdmin(quizId, questionNumber);
        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/quiz-categories")
    public ResponseEntity<?> deleteAllQuizCategories() {
        quizCategoryService.deleteAllQuizCategories();
        return ResponseEntity.ok("All Categories has been deleted");
    }

    @DeleteMapping("/quiz-category/{categoryId}")
    public ResponseEntity<?> deleteQuizCategory(@PathVariable Long categoryId) {
        quizCategoryService.deleteQuizCategoryById(categoryId);
        return ResponseEntity.ok("Category has been deleted");
    }
}
