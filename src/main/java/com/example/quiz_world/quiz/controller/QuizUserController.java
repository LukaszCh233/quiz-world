package com.example.quiz_world.quiz.controller;

import com.example.quiz_world.quiz.dto.QuizResultDTO;
import com.example.quiz_world.quiz.entity.Question;
import com.example.quiz_world.quiz.entity.Quiz;
import com.example.quiz_world.quiz.entity.UserAnswer;
import com.example.quiz_world.quiz.service.QuizQuestionService;
import com.example.quiz_world.quiz.service.QuizResultService;
import com.example.quiz_world.quiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class QuizUserController {
    private final QuizService quizService;

    private final QuizQuestionService quizQuestionService;
    private final QuizResultService quizResultService;

    public QuizUserController(QuizService quizService, QuizQuestionService quizQuestionService,
                              QuizResultService quizResultService) {
        this.quizService = quizService;
        this.quizQuestionService = quizQuestionService;
        this.quizResultService = quizResultService;
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
    public ResponseEntity<?> solveQuiz(@PathVariable Long quizId, @RequestBody List<UserAnswer> userAnswersToQuiz, Principal principal) {
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
}
