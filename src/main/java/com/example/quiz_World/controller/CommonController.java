package com.example.quiz_World.controller;

import com.example.quiz_World.dto.*;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.service.category.QuizCategoryService;
import com.example.quiz_World.service.category.WordSetCategoryService;
import com.example.quiz_World.service.quiz.QuizQuestionService;
import com.example.quiz_World.service.quiz.QuizService;
import com.example.quiz_World.service.words.WordSetService;
import com.example.quiz_World.service.words.WordsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {
    private final WordSetCategoryService wordSetCategoryService;
    private final QuizService quizService;
    private final QuizQuestionService quizQuestionService;
    private final WordsService wordService;
    private final WordSetService wordSetService;
    private final QuizCategoryService quizCategoryService;

    public CommonController(WordSetCategoryService wordSetCategoryService, QuizService quizService,
                            QuizQuestionService quizQuestionService, WordsService wordService,
                            WordSetService wordSetService, QuizCategoryService quizCategoryService) {
        this.wordSetCategoryService = wordSetCategoryService;
        this.quizService = quizService;
        this.quizQuestionService = quizQuestionService;
        this.wordService = wordService;
        this.wordSetService = wordSetService;
        this.quizCategoryService = quizCategoryService;
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

    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<?> displayQuestions(@PathVariable Long quizId) {
        List<QuestionDTO> questions = quizQuestionService.findQuestionsByQuizId(quizId);

        return ResponseEntity.ok(questions);
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody Quiz quiz, Principal principal) {
        QuizDTO createQuiz = quizService.createQuiz(quiz.getTitle(), quiz.getQuizCategory().getId(), quiz.getStatus(), principal);

        return ResponseEntity.ok(createQuiz);
    }

    @PostMapping("/quiz/{quizId}/question")
    public ResponseEntity<?> addQuestionToQuiz(@PathVariable Long quizId, @RequestBody Question question) {
        quizQuestionService.addQuestionsToQuiz(quizId, question);

        return ResponseEntity.ok("Question added to quiz successfully");
    }

    @GetMapping("/user-quizzes")
    public ResponseEntity<?> displayYourQuizzes(Principal principal) {
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

    @GetMapping("/wordSetCategories")
    public ResponseEntity<List<WordSetCategoryDTO>> displayWordSetCategories() {
        List<WordSetCategoryDTO> wordSetCategoryDTOList = wordSetCategoryService.findAllWordSetCategories();

        return ResponseEntity.ok(wordSetCategoryDTOList);
    }

    @GetMapping("/wordSets")
    public ResponseEntity<?> displayWordSets() {
        List<WordSetDTO> wordSetDTOList = wordSetService.findPublicWordSets();

        return ResponseEntity.ok(wordSetDTOList);
    }

    @PostMapping("/wordSet")
    public ResponseEntity<WordSetDTO> createWordSet(@RequestBody WordSet wordSet, Principal principal) {
        WordSetDTO createWordSet = wordSetService.createWordSet(wordSet.getTitle(), wordSet.getWordSetCategory().getId(), wordSet.getStatus(), principal);

        return ResponseEntity.ok(createWordSet);
    }

    @PostMapping("/wordSet/{wordSetId}/word")
    public ResponseEntity<?> addWordToWordSet(@PathVariable Long wordSetId, @RequestBody Word word) {
        wordService.addWordToWordSet(wordSetId, word);

        return ResponseEntity.ok("Word added to word set successfully");
    }

    @GetMapping("/user-WordSets")
    public ResponseEntity<List<WordSetDTO>> displayYourWordSets(Principal principal) {
        List<WordSetDTO> wordSetDTOS = wordSetService.findYourWordSets(principal);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/wordSets/category{categoryId}")
    public ResponseEntity<?> displayWordSetsByCategory(@PathVariable Long categoryId) {
        List<WordSetDTO> wordSetDTOS = wordSetService.findWordSetByCategory(categoryId);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/wordSet/{wordSetId}/words")
    public ResponseEntity<?> displayWords(@PathVariable Long wordSetId) {
        List<WordDTO> words = wordService.findWordsByWordSetId(wordSetId);

        return ResponseEntity.ok(words);
    }

    @GetMapping("/wordSet/{id}")
    public ResponseEntity<?> displayWordSet(@PathVariable Long id) {
        WordSetDTO wordSetDTO = wordSetService.findWordSetById(id);

        return ResponseEntity.ok(wordSetDTO);
    }
}
