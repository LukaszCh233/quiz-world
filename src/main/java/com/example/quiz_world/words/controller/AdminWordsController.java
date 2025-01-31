package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.word.WordRequest;
import com.example.quiz_world.words.word.WordsService;
import com.example.quiz_world.words.wordSet.WordSetRequest;
import com.example.quiz_world.words.wordSet.WordSetService;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryDTO;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryRequest;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminWordsController {
    private final WordSetCategoryService wordSetCategoryService;
    private final WordsService wordService;
    private final WordSetService wordSetService;

    public AdminWordsController(WordSetCategoryService wordSetCategoryService, WordsService wordService,
                                WordSetService wordSetService) {
        this.wordSetCategoryService = wordSetCategoryService;
        this.wordService = wordService;
        this.wordSetService = wordSetService;
    }

    @PostMapping("/wordSet-category")
    public ResponseEntity<WordSetCategoryDTO> createWordSetCategory(@Valid @RequestBody
                                                                    WordSetCategoryRequest wordSetCategoryRequest) {
        WordSetCategoryDTO createWordSetCategory = wordSetCategoryService.createWordSetCategory(wordSetCategoryRequest);

        return ResponseEntity.ok(createWordSetCategory);
    }

    @PutMapping("/wordSet/{wordSetId}")
    public ResponseEntity<String> updateWordSet(@PathVariable Long wordSetId,
                                                @Valid @RequestBody WordSetRequest wordSetRequest) {
        wordSetService.updateWordSetByIdForAdmin(wordSetId, wordSetRequest);

        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<String> updateWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber,
                                             @Valid @RequestBody WordRequest wordRequest) {
        wordService.updateWordForAdmin(wordSetId, wordNumber, wordRequest);

        return ResponseEntity.ok("Word has been updated");
    }

    @DeleteMapping("/wordSets")
    public ResponseEntity<String> deleteAllWordSets() {
        wordSetService.deleteAllWordSetsForAdmin();

        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}")
    public ResponseEntity<String> deleteWordSet(@PathVariable Long wordSetId) {
        wordSetService.deleteWordSetByIdForAdmin(wordSetId);

        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<String> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber) {
        wordService.deleteWordByNumberAndWordSetForAdmin(wordSetId, wordNumber);

        return ResponseEntity.ok("Word has been deleted");
    }

    @DeleteMapping("/wordSet-category/{categoryId}")
    public ResponseEntity<String> deleteWordSetCategory(@PathVariable Long categoryId) {
        wordSetCategoryService.deleteWordSetCategoryById(categoryId);

        return ResponseEntity.ok("Category has been deleted");
    }

    @DeleteMapping("/wordSet-categories")
    public ResponseEntity<String> deleteAllWordSetCategories() {
        wordSetCategoryService.deleteAllWordSetCategories();

        return ResponseEntity.ok("All Categories has been deleted");
    }

    @PutMapping("/wordSet-category/{categoryId}")
    public ResponseEntity<WordSetCategoryDTO> updateWordSetCategory(@PathVariable Long categoryId,
                                                                    @Valid @PathVariable WordSetCategoryRequest wordSetCategory) {
        WordSetCategoryDTO updateCategory = wordSetCategoryService.updateWordSetCategory(categoryId, wordSetCategory);

        return ResponseEntity.ok(updateCategory);
    }
}
