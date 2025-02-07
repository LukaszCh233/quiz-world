package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.word.WordDTO;
import com.example.quiz_world.words.word.WordRequest;
import com.example.quiz_world.words.word.WordsService;
import com.example.quiz_world.words.wordSet.*;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryDTO;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonWordsController {

    private final WordSetCategoryService wordSetCategoryService;
    private final WordsService wordService;
    private final WordSetService wordSetService;

    public CommonWordsController(WordSetCategoryService wordSetCategoryService, WordsService wordService,
                                 WordSetService wordSetService) {
        this.wordSetCategoryService = wordSetCategoryService;
        this.wordService = wordService;
        this.wordSetService = wordSetService;
    }

    @GetMapping("/wordSetCategories")
    public ResponseEntity<List<WordSetCategoryDTO>> displayWordSetCategories() {
        List<WordSetCategoryDTO> wordSetCategoryDTOList = wordSetCategoryService.findAllWordSetCategories();

        return ResponseEntity.ok(wordSetCategoryDTOList);
    }

    @GetMapping("/wordSets")
    public ResponseEntity<List<WordSetDTO>> displayWordSets() {
        List<WordSetDTO> wordSetDTOList = wordSetService.findPublicWordSets();

        return ResponseEntity.ok(wordSetDTOList);
    }

    @PostMapping("/wordSet")
    public ResponseEntity<WordSetDTO> createWordSet(@Valid @RequestBody WordSetRequest wordSetRequest,
                                                    Principal principal) {
        WordSetDTO createWordSet = wordSetService.createWordSet(wordSetRequest, principal);

        return ResponseEntity.ok(createWordSet);
    }

    @PostMapping("/wordSet/{wordSetId}/word")
    public ResponseEntity<String> addWordToWordSet(@PathVariable Long wordSetId, @Valid @RequestBody WordRequest word) {
        wordService.addWordToWordSet(wordSetId, word);

        return ResponseEntity.ok("Word added to word set successfully");
    }

    @GetMapping("/user-WordSets")
    public ResponseEntity<List<WordSetDTO>> displayYourWordSets(Principal principal) {
        List<WordSetDTO> wordSetDTOS = wordSetService.findWordSetsByUserPrincipal(principal);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/wordSets/category{categoryId}")
    public ResponseEntity<List<WordSetDTO>> displayWordSetsByCategory(@PathVariable Long categoryId) {
        List<WordSetDTO> wordSetDTOS = wordSetService.findWordSetByCategoryId(categoryId);

        return ResponseEntity.ok(wordSetDTOS);
    }

    @GetMapping("/wordSet/{wordSetId}/words")
    public ResponseEntity<List<WordDTO>> displayWords(@PathVariable Long wordSetId) {
        List<WordDTO> words = wordService.findWordsByWordSetId(wordSetId);

        return ResponseEntity.ok(words);
    }

    @GetMapping("/wordSet/{id}")
    public ResponseEntity<WordSetDTO> displayWordSet(@PathVariable Long id) {
        WordSetDTO wordSetDTO = wordSetService.findWordSetById(id);

        return ResponseEntity.ok(wordSetDTO);
    }

    @PostMapping("/wordSet-solve/{wordSetId}")
    public ResponseEntity<String> solveFLashCard(@PathVariable Long wordSetId,
                                                 @RequestBody @Valid List<AnswerToWordSet> userAnswers,
                                                 Principal principal) {
        double score = wordSetService.solveWordSet(wordSetId, userAnswers, principal);

        return ResponseEntity.ok("Your score: " + score);
    }

    @GetMapping("/words/score")
    public ResponseEntity<List<WordSetResultDTO>> displayYourWordSetsScore(Principal principal) {
        List<WordSetResultDTO> wordSetResultDTOS = wordService.findYourWordsResults(principal);

        return ResponseEntity.ok(wordSetResultDTOS);
    }
}
