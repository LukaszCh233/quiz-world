package com.example.quiz_World.controller;

import com.example.quiz_World.dto.QuizResultDTO;
import com.example.quiz_World.dto.WordSetResultDTO;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.UserAnswer;
import com.example.quiz_World.entities.wordSetEntity.AnswerToWordSet;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.service.quiz.QuizQuestionService;
import com.example.quiz_World.service.quiz.QuizResultService;
import com.example.quiz_World.service.quiz.QuizService;
import com.example.quiz_World.service.words.WordSetService;
import com.example.quiz_World.service.words.WordsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final QuizService quizService;
    private final WordsService wordService;
    private final QuizQuestionService quizQuestionService;
    private final QuizResultService quizResultService;
    private final WordSetService wordSetService;


    public UserController(QuizService quizService, WordsService wordService,
                          QuizQuestionService quizQuestionService, QuizResultService quizResultService,
                          WordSetService wordSetService) {
        this.quizService = quizService;
        this.wordService = wordService;
        this.quizQuestionService = quizQuestionService;
        this.quizResultService = quizResultService;
        this.wordSetService = wordSetService;
    }

    @DeleteMapping("/quizzes")
    public ResponseEntity<?> deleteYourQuizzes(Principal principal) {
        quizService.deleteAllQuizzesForUser(principal);

        return ResponseEntity.ok("All quizzes has been deleted");
    }

    @DeleteMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber, Principal principal) {
        quizQuestionService.deleteQuestionByNumberQuestionForUser(quizId, questionNumber, principal);

        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<?> deleteYourQuiz(@PathVariable Long quizId, Principal principal) {
        quizService.deleteQuizByIdForUser(quizId, principal);

        return ResponseEntity.ok("Quiz has been deleted");
    }

    @PutMapping("/quiz/{quizId}")
    public ResponseEntity<?> updateYourQuiz(@PathVariable Long quizId, @Valid @RequestBody Quiz quiz, Principal principal) {
        quizService.updateQuizByIdForUser(quizId, quiz, principal);

        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<?> updateYourQuizQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber, @Valid @RequestBody Question questions, Principal principal) {
        quizQuestionService.updateQuestionByQuestionNumberForUser(quizId, questionNumber, questions, principal);

        return ResponseEntity.ok("Question updated");
    }

    @PostMapping("/quiz-solve/{quizId}")
    public ResponseEntity<?> solveQuiz(@PathVariable Long quizId,@RequestBody List<UserAnswer> userAnswersToQuiz, Principal principal) {
        double score = quizService.solveQuiz(quizId, userAnswersToQuiz, principal);

        return ResponseEntity.ok("Quiz solved successfully. Your score: " + score);
    }

    @GetMapping("/quiz/score")
    public ResponseEntity<List<QuizResultDTO>> displayYourQuizzesScore(Principal principal) {
        List<QuizResultDTO> quizResultDTOS = quizResultService.findYourQuizzesResults(principal);

        return ResponseEntity.ok(quizResultDTOS);
    }

    @GetMapping("/quiz/globalScore")
    public ResponseEntity<List<QuizResultDTO>> displayQuizzesScore() {
        List<QuizResultDTO> quizResultDTOS = quizResultService.findQuizzesResults();

        return ResponseEntity.ok(quizResultDTOS);
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

