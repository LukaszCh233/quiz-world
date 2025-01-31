package com.example.quiz_world.quiz.controller;

import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.question.QuizQuestionService;
import com.example.quiz_world.quiz.quiz.QuizRequest;
import com.example.quiz_world.quiz.quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class QuizUserController {
    private final QuizService quizService;
    private final QuizQuestionService quizQuestionService;

    public QuizUserController(QuizService quizService, QuizQuestionService quizQuestionService) {
        this.quizService = quizService;
        this.quizQuestionService = quizQuestionService;
    }

    @DeleteMapping("/quizzes")
    public ResponseEntity<String> deleteYourQuizzes(Principal principal) {
        quizService.deleteAllQuizzesForUser(principal);

        return ResponseEntity.ok("All quizzes has been deleted");
    }

    @DeleteMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber,
                                                 Principal principal) {
        quizQuestionService.deleteQuestionByNumberQuestionForUser(quizId, questionNumber, principal);

        return ResponseEntity.ok("Question has been deleted");
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<String> deleteYourQuiz(@PathVariable Long quizId, Principal principal) {
        quizService.deleteQuizByIdForUser(quizId, principal);

        return ResponseEntity.ok("Quiz has been deleted");
    }

    @PutMapping("/quiz/{quizId}")
    public ResponseEntity<String> updateYourQuiz(@PathVariable Long quizId, @Valid @RequestBody QuizRequest quizRequest,
                                                 Principal principal) {
        quizService.updateQuizByIdForUser(quizId, quizRequest, principal);

        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("/quiz/{quizId}/question/{questionNumber}")
    public ResponseEntity<String> updateYourQuizQuestion(@PathVariable Long quizId, @PathVariable Long questionNumber,
                                                         @Valid @RequestBody Question questions, Principal principal) {
        quizQuestionService.updateQuestionByQuestionNumberForUser(quizId, questionNumber, questions, principal);

        return ResponseEntity.ok("Question updated");
    }
}
