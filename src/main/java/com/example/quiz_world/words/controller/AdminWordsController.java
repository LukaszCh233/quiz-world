package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.entity.Word;
import com.example.quiz_world.words.entity.WordSet;
import com.example.quiz_world.words.entity.WordSetCategory;
import com.example.quiz_world.words.service.WordSetCategoryService;
import com.example.quiz_world.words.service.WordSetService;
import com.example.quiz_world.words.service.WordsService;
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
    public ResponseEntity<WordSetCategory> createWordSetCategory(@Valid @RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory createWordSetCategory = wordSetCategoryService.createWordSetCategory(wordSetCategory);

        return ResponseEntity.ok(createWordSetCategory);
    }

    @PutMapping("/wordSet/{wordSetId}")
    public ResponseEntity<String> updateWordSet(@PathVariable Long wordSetId, @Valid @RequestBody WordSet wordSet) {
        wordSetService.updateWordSetByIdForAdmin(wordSetId, wordSet);

        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<String> updateWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, @Valid @RequestBody Word word) {
        wordService.updateWordForAdmin(wordSetId, wordNumber, word);

        return ResponseEntity.ok("Word updated");
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
        wordService.deleteWordByNumberWordSetForAdmin(wordSetId, wordNumber);

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
    public ResponseEntity<WordSetCategory> updateWordSetCategory(@PathVariable Long categoryId, @Valid @RequestBody WordSetCategory wordSetCategory) {
        WordSetCategory updateCategory = wordSetCategoryService.updateWordSetCategory(categoryId, wordSetCategory);

        return ResponseEntity.ok(updateCategory);
    }
}
