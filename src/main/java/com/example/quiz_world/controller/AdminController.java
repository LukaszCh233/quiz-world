package com.example.quiz_world.controller;

import com.example.quiz_world.dto.AdminDTO;
import com.example.quiz_world.dto.UserDTO;
import com.example.quiz_world.entities.quizEntity.Question;
import com.example.quiz_world.entities.quizEntity.Quiz;
import com.example.quiz_world.entities.quizEntity.QuizCategory;
import com.example.quiz_world.entities.wordSetEntity.Word;
import com.example.quiz_world.entities.wordSetEntity.WordSet;
import com.example.quiz_world.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_world.service.AdminService;
import com.example.quiz_world.service.UserService;
import com.example.quiz_world.service.category.QuizCategoryService;
import com.example.quiz_world.service.category.WordSetCategoryService;
import com.example.quiz_world.service.quiz.QuizQuestionService;
import com.example.quiz_world.service.quiz.QuizService;
import com.example.quiz_world.service.words.WordSetService;
import com.example.quiz_world.service.words.WordsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;
    private final QuizService quizService;
    private final WordSetCategoryService wordSetCategoryService;
    private final QuizCategoryService quizCategoryService;
    private final WordsService wordService;
    private final QuizQuestionService quizQuestionService;
    private final WordSetService wordSetService;

    public AdminController(UserService userService, AdminService adminService, QuizService quizService,
                           WordSetCategoryService wordSetCategoryService, WordsService wordService,
                           QuizQuestionService quizQuestionService, WordSetService wordSetService,
                           QuizCategoryService quizCategoryService) {
        this.userService = userService;
        this.adminService = adminService;
        this.quizService = quizService;
        this.wordSetCategoryService = wordSetCategoryService;
        this.wordService = wordService;
        this.quizQuestionService = quizQuestionService;
        this.wordSetService = wordSetService;
        this.quizCategoryService = quizCategoryService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<AdminDTO> admins = adminService.findAllAdmins();
        return ResponseEntity.ok(admins);
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

    @PostMapping("/quiz-category")
    public ResponseEntity<QuizCategory> createQuizCategory(@Valid @RequestBody QuizCategory quizCategory) {
        QuizCategory createQuizCategory = quizCategoryService.createQuizCategory(quizCategory);

        return ResponseEntity.ok(createQuizCategory);
    }

    @PostMapping("/wordSet-category")
    public ResponseEntity<WordSetCategory> createWordSetCategory(@Valid @RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory createWordSetCategory = wordSetCategoryService.createWordSetCategory(wordSetCategory);

        return ResponseEntity.ok(createWordSetCategory);
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

    @DeleteMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber) {
        quizQuestionService.deleteQuestionByNumberQuestionForAdmin(quizId, questionNumber);
        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/quiz-category/{categoryId}")
    public ResponseEntity<?> deleteQuizCategory(@PathVariable Long categoryId) {
        quizCategoryService.deleteQuizCategoryById(categoryId);
        return ResponseEntity.ok("Category has been deleted");
    }

    @DeleteMapping("/quiz-categories")
    public ResponseEntity<?> deleteAllQuizCategories() {
        quizCategoryService.deleteAllQuizCategories();
        return ResponseEntity.ok("All Categories has been deleted");
    }

    @PutMapping("/wordSet/{wordSetId}")
    public ResponseEntity<?> updateWordSet(@PathVariable Long wordSetId, @Valid @RequestBody WordSet wordSet) {
        wordSetService.updateWordSetByIdForAdmin(wordSetId, wordSet);
        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<?> updateWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, @Valid @RequestBody Word word) {
        wordService.updateWordForAdmin(wordSetId, wordNumber, word);
        return ResponseEntity.ok("Word updated");
    }

    @DeleteMapping("/wordSets")
    public ResponseEntity<?> deleteAllWordSets() {
        wordSetService.deleteAllWordSetsForAdmin();
        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}")
    public ResponseEntity<?> deleteWordSet(@PathVariable Long wordSetId) {
        wordSetService.deleteWordSetByIdForAdmin(wordSetId);
        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber) {
        wordService.deleteWordByNumberWordSetForAdmin(wordSetId, wordNumber);
        return ResponseEntity.ok("Word has been deleted");
    }

    @DeleteMapping("/wordSet-category/{categoryId}")
    public ResponseEntity<?> deleteWordSetCategory(@PathVariable Long categoryId) {
        wordSetCategoryService.deleteWordSetCategoryById(categoryId);
        return ResponseEntity.ok("Category has been deleted");
    }

    @DeleteMapping("/wordSet-categories")
    public ResponseEntity<?> deleteAllWordSetCategories() {
        wordSetCategoryService.deleteAllWordSetCategories();
        return ResponseEntity.ok("All Categories has been deleted");
    }

    @PutMapping("/wordSet-category/{categoryId}")
    public ResponseEntity<?> updateWordSetCategory(@PathVariable Long categoryId, @Valid @RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory updateCategory = wordSetCategoryService.updateWordSetCategory(categoryId, wordSetCategory);
        return ResponseEntity.ok(updateCategory);
    }

    @PutMapping("/quiz-category/{categoryId}")
    public ResponseEntity<?> updateQuizCategory(@PathVariable Long categoryId, @Valid @RequestBody QuizCategory quizCategory) {
        QuizCategory updateCategory = quizCategoryService.updateQuizCategory(categoryId, quizCategory);
        return ResponseEntity.ok(updateCategory);
    }
}
