package com.example.quiz_World.controller;

import com.example.quiz_World.entities.QuizResultDTO;
import com.example.quiz_World.entities.WordSetResultDTO;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.UserAnswer;
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

        return ResponseEntity.ok("All quizzes has been deleted");
    }

    @DeleteMapping("/deleteQuestion/{quizId}/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber, Principal principal) {
        quizService.deleteQuestionByNumberQuestionForUser(quizId, questionNumber, principal);

        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/deleteQuiz/{quizId}")
    public ResponseEntity<?> deleteYourQuiz(@PathVariable Long quizId, Principal principal) {
        quizService.deleteQuizByIdForUser(quizId, principal);

        return ResponseEntity.ok("Quiz has been deleted");
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
    public ResponseEntity<?> solveQuiz(@PathVariable Long quizId, @RequestBody List<UserAnswer> userAnswersToQuiz, Principal principal) {
        double score = quizService.solveQuiz(quizId, userAnswersToQuiz, principal);

        return ResponseEntity.ok("Quiz solved successfully. Your score: " + score);
    }

    @GetMapping("/quiz/score")
    public ResponseEntity<List<QuizResultDTO>> displayYourQuizzesScore(Principal principal) {
        List<QuizResultDTO> quizResultDTOS = quizService.findYourQuizzesResults(principal);

        return ResponseEntity.ok(quizResultDTOS);
    }
    @GetMapping("/quiz/globalScore")
    public ResponseEntity<List<QuizResultDTO>> displayQuizzesScore() {
        List<QuizResultDTO> quizResultDTOS = quizService.findQuizzesResults();

        return ResponseEntity.ok(quizResultDTOS);
    }
    @GetMapping("/words/score")
    public ResponseEntity<List<WordSetResultDTO>> displayYourWordSetsScore(Principal principal) {
        List<WordSetResultDTO> wordSetResultDTOS = wordService.findYourWordsResults(principal);

        return ResponseEntity.ok(wordSetResultDTOS);
    }

    @PostMapping("/solveWordSet/{wordSetId}")
    public ResponseEntity<?> solveFLashCard(@PathVariable Long wordSetId, @RequestBody List<AnswerToWordSet> userAnswers, Principal principal) {
        double score = wordService.solveWordSet(wordSetId, userAnswers, principal);

        return ResponseEntity.ok("Your score: " + score);
    }

    @Transactional
    @DeleteMapping("/deleteWordSets")
    public ResponseEntity<?> deleteAllWordSet(Principal principal) {
        wordService.deleteAllWordSetsForUser(principal);

        return ResponseEntity.ok("All word sets has been deleted");
    }

    @DeleteMapping("/deleteWordSet/{wordSetId}")
    public ResponseEntity<?> deleteWordSet(@PathVariable Long wordSetId, Principal principal) {
        wordService.deleteWordSetByIdForUser(wordSetId, principal);

        return ResponseEntity.ok("Word set has been deleted");
    }

    @DeleteMapping("/deleteWord/{wordSetId}/{wordNumber}")
    public ResponseEntity<?> deleteWord(@PathVariable Long wordSetId, @PathVariable Long wordNumber, Principal principal) {
        wordService.deleteWordByNumberWordSetForUser(wordSetId, wordNumber, principal);

        return ResponseEntity.ok("Word has been deleted");
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

