package com.example.quiz_World.controller;

import com.example.quiz_World.entities.ResultDTO;
import com.example.quiz_World.entities.quizEntity.AnswerToQuiz;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.wordSetEntity.AnswerToWordSet;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.service.QuizServiceImpl;
import com.example.quiz_World.service.WordServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final QuizServiceImpl quizService;
    private final WordServiceImpl wordService;


    public UserController(QuizServiceImpl quizService, WordServiceImpl wordService) {
        this.quizService = quizService;
        this.wordService = wordService;
    }

    @DeleteMapping("/deleteQuizzes")
    public ResponseEntity<?> deleteYourQuizzes(Principal principal) {
        quizService.deleteAllQuizzesForUser(principal);

        return ResponseEntity.ok("All Quizzes have been deleted");
    }

    @DeleteMapping("/deleteQuestion/{quizId}/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber, Principal principal) {
        quizService.deleteQuestionByNumberQuestionForUser(quizId, questionNumber, principal);

        return ResponseEntity.ok("Question deleted");
    }

    @DeleteMapping("/deleteQuiz/{quizId}")
    public ResponseEntity<?> deleteYourQuiz(@PathVariable Long quizId, Principal principal) {
        quizService.deleteQuizByIdForUser(quizId, principal);

        return ResponseEntity.ok("Quiz deleted");
    }

    @PutMapping("/updateQuiz/{quizId}")
    public ResponseEntity<?> updateYourQuiz(@PathVariable Long quizId, @RequestBody Quiz quiz, Principal principal) {
        quizService.updateQuizByIdForUser(quizId, quiz, principal);

        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("/updateQuizQuestion/{quizId}/{questionNumber}")
    public ResponseEntity<?> updateYourQuizQuestions(@PathVariable Long quizId, @PathVariable Long questionNumber, @RequestBody Question questions, Principal principal) {
        quizService.updateQuestionByQuestionNumberForUser(quizId, questionNumber, questions, principal);

        return ResponseEntity.ok("Question updated");
    }

    @PostMapping("/solveQuiz/{quizId}")
    public ResponseEntity<?> solveQuiz(@PathVariable Long quizId, @RequestBody List<AnswerToQuiz> userAnswerToQuizs, Principal principal) {
        double score = quizService.solveQuiz(quizId, userAnswerToQuizs, principal);

        return ResponseEntity.ok("Quiz solved successfully. Your score: " + score);
    }

    @GetMapping("/score")
    public ResponseEntity<List<ResultDTO>> displayQuizzesScore(Principal principal) {
        List<ResultDTO> resultDTOS = quizService.findQuizzesResults(principal);

        return ResponseEntity.ok(resultDTOS);
    }

    @PostMapping("/solveWordSet/{wordSetId}")
    public ResponseEntity<?> solveFLashCard(@PathVariable Long wordSetId, @RequestBody List<AnswerToWordSet> userAnswers, Principal principal) {
        double score = wordService.solveWordSet(wordSetId, userAnswers, principal);

        return ResponseEntity.ok("Your score: " + score);
    }

    @Transactional
    @DeleteMapping("/deleteWordSets")
    public ResponseEntity<?> deleteAllWordSet(Principal principal) {
        wordService.deleteAllWordSetForUser(principal);

        return ResponseEntity.ok("Word sets has been deleted");
    }

    @DeleteMapping("/deleteWordSet/{wordSetId}")
    public ResponseEntity<?> deleteWordSet(@PathVariable Long wordSetId, Principal principal) {
        wordService.deleteWordSetById(wordSetId, principal);

        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/deleteWord/{wordSetId}/{wordNumber}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, Principal principal) {
        wordService.deleteWordByNumberWordSetForUser(wordSetId, wordNumber, principal);

        return ResponseEntity.ok("Word deleted");
    }

    @PutMapping("/updateWordSet/{wordSetId}")
    public ResponseEntity<?> updateYourWordSet(@PathVariable Long wordSetId, @RequestBody WordSet wordSet, Principal principal) {
        wordService.updateWordSetByIdForUser(wordSetId, wordSet, principal);

        return ResponseEntity.ok("Word set updated");
    }

    @PutMapping("/updateWord/{wordSetId}/{wordNumber}")
    public ResponseEntity<?> updateWord(@PathVariable Long wordSetId, @RequestBody Word word, @PathVariable Long wordNumber, Principal principal) {
        wordService.updateWordForUser(wordSetId, wordNumber, word, principal);

        return ResponseEntity.ok("Word updated");
    }
}

