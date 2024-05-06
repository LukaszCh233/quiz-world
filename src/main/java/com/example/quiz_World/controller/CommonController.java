package com.example.quiz_World.controller;

import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.entities.wordSetEntity.*;
import com.example.quiz_World.service.CategoryServiceImp;
import com.example.quiz_World.service.QuizServiceImpl;
import com.example.quiz_World.service.WordServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {
    private final CategoryServiceImp categoryService;
    private final QuizServiceImpl quizService;
    private final WordServiceImpl wordService;

    public CommonController(CategoryServiceImp categoryService, QuizServiceImpl quizService, WordServiceImpl wordService) {
        this.categoryService = categoryService;
        this.quizService = quizService;
        this.wordService = wordService;
    }

    @GetMapping("/quizzes")
    public ResponseEntity<?> displayQuizzes() {
        List<QuizDTO> quizzes = quizService.findAllPublicQuizzes();

        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> displayQuiz(@PathVariable Long id) {
        QuizDTO quizDTO = quizService.findQuizById(id);

        return ResponseEntity.ok(quizDTO);
    }

    @GetMapping("/questions/{quizId}")
    public ResponseEntity<?> displayQuestions(@PathVariable Long quizId) {
        List<QuestionDTO> questions = quizService.findQuestionsByQuizId(quizId);

        return ResponseEntity.ok(questions);
    }

    @PostMapping("/createQuiz")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody Quiz quiz, Principal principal) {

        QuizDTO createQuiz = quizService.createQuiz(quiz.getTitle(), quiz.getQuizCategory(), quiz.getStatus(), principal);

        return ResponseEntity.ok(createQuiz);
    }

    @PostMapping("/addQuestionsToQuiz/{quizId}")
    public ResponseEntity<?> addQuestionsToQuiz(@PathVariable Long quizId, @RequestBody Question questions) {
        quizService.addQuestionsToQuiz(quizId, questions);

        return ResponseEntity.ok("Question added to quiz successfully");
    }

    @GetMapping("/yourQuizzes")
    public ResponseEntity<?> displayYourQuizzes(Principal principal) {
        List<QuizDTO> quizzes = quizService.findYourQuizzes(principal);

        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/Quizzes/{categoryId}")
    public ResponseEntity<List<QuizDTO>> displayQuizzesByCategory(@PathVariable Long categoryId) {
        List<QuizDTO> quizDTOList = quizService.findQuizByCategoryId(categoryId);

        return ResponseEntity.ok(quizDTOList);
    }

    @GetMapping("/quizCategories")
    public ResponseEntity<List<QuizCategory>> displayQuizCategories() {
        List<QuizCategory> quizCategoryList = categoryService.findAllQuizCategories();

        return ResponseEntity.ok(quizCategoryList);
    }

    @GetMapping("/wordSetCategories")
    public ResponseEntity<List<WordSetCategory>> displayWordSetCategories() {
        List<WordSetCategory> wordSetCategoryList = categoryService.findAllWordSetCategories();

        return ResponseEntity.ok(wordSetCategoryList);
    }

    @GetMapping("/wordSets")
    public ResponseEntity<?> displayWordSets() {
        List<WordSetDTO> wordSetDTOList = wordService.findPublicWordSet();

        return ResponseEntity.ok(wordSetDTOList);
    }

    @PostMapping("/createWordSet")
    public ResponseEntity<?> createWordSet(@RequestBody WordSet wordSet, Principal principal) {
        WordSetDTO createWordSet = wordService.createWordSet(wordSet.getTitle(), wordSet.getWordSetCategory().getId(), wordSet.getStatus(), principal);

        return ResponseEntity.ok(createWordSet);
    }

    @PostMapping("/addWordToWordSet/{wordSetId}")
    public ResponseEntity<?> addWordToWordSet(@PathVariable Long wordSetId, @RequestBody Word word) {
        wordService.addWordToWordSet(wordSetId, word);

        return ResponseEntity.ok("Word added to word set successfully");
    }

    @GetMapping("/yourWordsSets")
    public ResponseEntity<List<WordSetDTO>> displayYourWordSets(Principal principal) {
        List<WordSetDTO> wordSetDTOS = wordService.findYourWordSets(principal);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/wordSets/{categoryId}")
    public ResponseEntity<?> displayWordSetsByCategory(@PathVariable Long categoryId) {
        List<WordSetDTO> wordSetDTOS = wordService.findWordSetByCategory(categoryId);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/words/{wordSetId}")
    public ResponseEntity<?> displayWords(@PathVariable Long wordSetId) {
        List<WordDTO> words = wordService.findWordsByWordSetId(wordSetId);

        return ResponseEntity.ok(words);
    }
}
