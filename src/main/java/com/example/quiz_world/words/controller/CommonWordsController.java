package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.dto.WordDTO;
import com.example.quiz_world.words.dto.WordSetCategoryDTO;
import com.example.quiz_world.words.dto.WordSetDTO;
import com.example.quiz_world.words.dto.WordSetResultDTO;
import com.example.quiz_world.words.entity.AnswerToWordSet;
import com.example.quiz_world.words.entity.Word;
import com.example.quiz_world.words.entity.WordSet;
import com.example.quiz_world.words.service.WordSetCategoryService;
import com.example.quiz_world.words.service.WordSetService;
import com.example.quiz_world.words.service.WordsService;
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
    public ResponseEntity<?> displayWordSets() {
        List<WordSetDTO> wordSetDTOList = wordSetService.findPublicWordSets();

        return ResponseEntity.ok(wordSetDTOList);
    }

    @PostMapping("/wordSet")
    public ResponseEntity<WordSetDTO> createWordSet(@Valid @RequestBody WordSet wordSet, Principal principal) {
        WordSetDTO createWordSet = wordSetService.createWordSet(wordSet.getTitle(), wordSet.getWordSetCategory().getId(), wordSet.getStatus(), principal);

        return ResponseEntity.ok(createWordSet);
    }

    @PostMapping("/wordSet/{wordSetId}/word")
    public ResponseEntity<?> addWordToWordSet(@PathVariable Long wordSetId, @Valid @RequestBody Word word) {
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

    @PostMapping("/wordSet-solve/{wordSetId}")
    public ResponseEntity<?> solveFLashCard(@PathVariable Long wordSetId, @RequestBody List<AnswerToWordSet> userAnswers, Principal principal) {
        double score = wordSetService.solveWordSet(wordSetId, userAnswers, principal);

        return ResponseEntity.ok("Your score: " + score);
    }

    @GetMapping("/words/score")
    public ResponseEntity<List<WordSetResultDTO>> displayYourWordSetsScore(Principal principal) {
        List<WordSetResultDTO> wordSetResultDTOS = wordService.findYourWordsResults(principal);

        return ResponseEntity.ok(wordSetResultDTOS);
    }
}
