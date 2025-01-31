package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.word.WordRequest;
import com.example.quiz_world.words.word.WordsService;
import com.example.quiz_world.words.wordSet.WordSetRequest;
import com.example.quiz_world.words.wordSet.WordSetService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserWordsController {
    private final WordsService wordService;
    private final WordSetService wordSetService;

    public UserWordsController(WordsService wordService, WordSetService wordSetService) {
        this.wordService = wordService;
        this.wordSetService = wordSetService;
    }

    @Transactional
    @DeleteMapping("/wordSets")
    public ResponseEntity<String> deleteAllWordSet(Principal principal) {
        wordSetService.deleteAllWordSetsForUser(principal);

        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}")
    public ResponseEntity<String> deleteWordSet(@PathVariable Long wordSetId, Principal principal) {
        wordSetService.deleteWordSetByIdForUser(wordSetId, principal);

        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<String> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber,
                                             Principal principal) {
        wordService.deleteWordByNumberAndWordSetForUser(wordSetId, wordNumber, principal);

        return ResponseEntity.ok("Word has been deleted");
    }

    @PutMapping("/wordSet/{wordSetId}")
    public ResponseEntity<String> updateYourWordSet(@PathVariable Long wordSetId,
                                                    @Valid @RequestBody WordSetRequest wordSetRequest,
                                                    Principal principal) {
        wordSetService.updateWordSetByIdForUser(wordSetId, wordSetRequest, principal);

        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<String> updateWord(@PathVariable Long wordSetId, @Valid @RequestBody WordRequest wordRequest,
                                             @PathVariable Long wordNumber, Principal principal) {
        wordService.updateWordForUser(wordSetId, wordNumber, wordRequest, principal);

        return ResponseEntity.ok("Word updated");
    }
}
