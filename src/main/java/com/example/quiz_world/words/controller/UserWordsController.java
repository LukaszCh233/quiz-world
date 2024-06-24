package com.example.quiz_world.words.controller;

import com.example.quiz_world.words.dto.WordSetResultDTO;
import com.example.quiz_world.words.entity.AnswerToWordSet;
import com.example.quiz_world.words.entity.Word;
import com.example.quiz_world.words.entity.WordSet;
import com.example.quiz_world.words.service.WordSetService;
import com.example.quiz_world.words.service.WordsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserWordsController {
    private final WordsService wordService;
    private final WordSetService wordSetService;

    public UserWordsController(WordsService wordService, WordSetService wordSetService) {
        this.wordService = wordService;
        this.wordSetService = wordSetService;
    }

    @GetMapping("/words/score")
    public ResponseEntity<List<WordSetResultDTO>> displayYourWordSetsScore(Principal principal) {
        List<WordSetResultDTO> wordSetResultDTOS = wordService.findYourWordsResults(principal);

        return ResponseEntity.ok(wordSetResultDTOS);
    }

    @PostMapping("/wordSet-solve/{wordSetId}")
    public ResponseEntity<?> solveFLashCard(@PathVariable Long wordSetId, @RequestBody List<AnswerToWordSet> userAnswers, Principal principal) {
        double score = wordSetService.solveWordSet(wordSetId, userAnswers, principal);

        return ResponseEntity.ok("Your score: " + score);
    }

    @Transactional
    @DeleteMapping("/wordSets")
    public ResponseEntity<?> deleteAllWordSet(Principal principal) {
        wordSetService.deleteAllWordSetsForUser(principal);

        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}")
    public ResponseEntity<?> deleteWordSet(@PathVariable Long wordSetId, Principal principal) {
        wordSetService.deleteWordSetByIdForUser(wordSetId, principal);

        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, Principal principal) {
        wordService.deleteWordByNumberWordSetForUser(wordSetId, wordNumber, principal);

        return ResponseEntity.ok("Word has been deleted");
    }

    @PutMapping("/wordSet/{wordSetId}")
    public ResponseEntity<?> updateYourWordSet(@PathVariable Long wordSetId, @Valid @RequestBody WordSet wordSet, Principal principal) {
        wordSetService.updateWordSetByIdForUser(wordSetId, wordSet, principal);

        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/wordSet/{wordSetId}/word/{wordNumber}")
    public ResponseEntity<?> updateWord(@PathVariable Long wordSetId, @Valid @RequestBody Word word, @PathVariable Long wordNumber, Principal principal) {
        wordService.updateWordForUser(wordSetId, wordNumber, word, principal);

        return ResponseEntity.ok("Word updated");
    }
}
