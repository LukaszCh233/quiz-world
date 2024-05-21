package com.example.quiz_World.controller;

import com.example.quiz_World.entities.UserDTO;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.service.CategoryServiceImp;
import com.example.quiz_World.service.QuizServiceImpl;
import com.example.quiz_World.service.UserServiceImpl;
import com.example.quiz_World.service.WordServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;
    private final QuizServiceImpl quizService;
    private final CategoryServiceImp categoryService;
    private final WordServiceImpl wordService;

    public AdminController(UserServiceImpl userService, QuizServiceImpl quizService, CategoryServiceImp categoryService,
                           WordServiceImpl wordService) {
        this.userService = userService;
        this.quizService = quizService;
        this.categoryService = categoryService;
        this.wordService = wordService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteQuizzes")
    public ResponseEntity<?> deleteAllQuizzes() {
        quizService.deleteAllQuizzesForAdmin();
        return ResponseEntity.ok("All Quizzes have been deleted");
    }

    @DeleteMapping("/deleteQuiz/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuizByIdForAdmin(quizId);
        return ResponseEntity.ok("Quiz have been deleted");
    }

    @PostMapping("/addQuizCategory")
    public ResponseEntity<QuizCategory> createQuizCategory(@RequestBody QuizCategory quizCategory) {
        QuizCategory createQuizCategory = categoryService.createQuizCategory(quizCategory);

        return ResponseEntity.ok(createQuizCategory);
    }

    @PostMapping("/addWordSetCategory")
    public ResponseEntity<WordSetCategory> createWordSetCategory(@RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory createWordSetCategory = categoryService.createWordSetCategory(wordSetCategory);

        return ResponseEntity.ok(createWordSetCategory);
    }

    @PutMapping("/updateQuiz/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quiz) {
        quizService.updateQuizByIdForAdmin(quizId, quiz);

        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("/updateQuizQuestion/{quizId}/{questionNumber}")
    public ResponseEntity<?> updateQuizQuestions(@PathVariable Long quizId, @PathVariable Long questionNumber, @RequestBody Question question) {
        quizService.updateQuestionByQuestionNumberForAdmin(quizId, questionNumber, question);

        return ResponseEntity.ok("Question updated");
    }

    @DeleteMapping("/deleteQuestion/{quizId}/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber) {
        quizService.deleteQuestionByNumberQuestionForAdmin(quizId, questionNumber);
        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/deleteQuizCategory/{categoryId}")
    public ResponseEntity<?> deleteQuizCategory(@PathVariable Long categoryId) {
        categoryService.deleteQuizCategoryById(categoryId);
        return ResponseEntity.ok("Category has been deleted");
    }

    @DeleteMapping("/deleteAllQuizCategories")
    public ResponseEntity<?> deleteAllQuizCategories() {
        categoryService.deleteAllQuizCategories();
        return ResponseEntity.ok("All Categories has been deleted");
    }

    @PutMapping("/updateWordSet/{wordSetId}")
    public ResponseEntity<?> updateWordSet(@PathVariable Long wordSetId, @RequestBody WordSet wordSet) {
        wordService.updateWordSetByIdForAdmin(wordSetId, wordSet);
        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/updateWord/{wordSetId}/{wordNumber}")
    public ResponseEntity<?> updateWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, @RequestBody Word word) {
        wordService.updateWordForAdmin(wordSetId, wordNumber, word);
        return ResponseEntity.ok("Word updated");
    }

    @DeleteMapping("/deleteWordSets")
    public ResponseEntity<?> deleteAllWordSets() {
        wordService.deleteAllWordSetsForAdmin();
        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/deleteWordSet/{wordSetId}")
    public ResponseEntity<?> deleteWordSet(@PathVariable Long wordSetId) {
        wordService.deleteWordSetByIdForAdmin(wordSetId);
        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/deleteWord/{wordSetId}/{wordNumber}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber) {
        wordService.deleteWordByNumberWordSetForAdmin(wordSetId, wordNumber);
        return ResponseEntity.ok("Word has been deleted");
    }

    @DeleteMapping("/deleteWordSetCategory/{categoryId}")
    public ResponseEntity<?> deleteWordSetCategory(@PathVariable Long categoryId) {
        categoryService.deleteWordSetCategoryById(categoryId);
        return ResponseEntity.ok("Category has been deleted");
    }

    @DeleteMapping("/deleteAllWordSetCategories")
    public ResponseEntity<?> deleteAllWordSetCategories() {
        categoryService.deleteAllWordSetCategories();
        return ResponseEntity.ok("All Categories has been deleted");
    }

    @PutMapping("/updateWordSetCategory/{categoryId}")
    public ResponseEntity<?> updateWordSetCategory(@PathVariable Long categoryId, @RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory updateCategory = categoryService.updateWordSetCategory(categoryId, wordSetCategory);
        return ResponseEntity.ok(updateCategory);
    }

    @PutMapping("/updateQuizCategory/{categoryId}")
    public ResponseEntity<?> updateQuizCategory(@PathVariable Long categoryId, @RequestBody QuizCategory quizCategory) {
        QuizCategory updateCategory = categoryService.updateQuizCategory(categoryId, quizCategory);
        return ResponseEntity.ok(updateCategory);
    }
}
