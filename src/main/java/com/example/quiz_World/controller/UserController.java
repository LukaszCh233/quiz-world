package com.example.quiz_World.controller;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.service.QuizServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final QuizServiceImpl quizService;

    public UserController(QuizServiceImpl quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/createQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz, Principal principal) {

        Quiz createQuiz = quizService.createQuiz(quiz.getTitle(), quiz.getCategory().getId(), quiz.getStatus(), principal);

        return ResponseEntity.ok(createQuiz);
    }

    @PostMapping("/addQuestionsToQuiz/{quizId}")
    public ResponseEntity<?> addQuestionsToQuiz(@PathVariable Long quizId, @RequestBody Question questions) {
        quizService.addQuestionsToQuiz(quizId, questions);
        return ResponseEntity.ok("Question added to quiz successfully");
    }

    @GetMapping("/getQuizzes")
    public ResponseEntity<?> displayQuizzes() {
        List<QuizDTO> quizzes = quizService.findAllPublicQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/getYourQuizzes")
    public ResponseEntity<?> displayYourQuizzes(Principal principal) {
        List<QuizDTO> quizzes = quizService.findYourQuizzes(principal);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/getQuiz/{id}")
    public ResponseEntity<?> displayQuiz(@PathVariable Long id) {
        QuizDTO quizDTO = quizService.findQuizById(id);
        return ResponseEntity.ok(quizDTO);
    }

    @GetMapping("/getQuestions/{quizId}")
    public ResponseEntity<?> displayQuestions(@PathVariable Long quizId) {
        List<QuestionDTO> questions = quizService.findQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteYourQuizzes(Principal principal) {
        quizService.deleteAllQuiz(principal);
        logger.info("git");
        return ResponseEntity.ok("All Quizzes have been deleted");
    }

    @DeleteMapping("/deleteQuiz/{quizId}")
    public ResponseEntity<?> deleteYourQuiz(@PathVariable Long quizId, Principal principal) {
        quizService.deleteQuizById(quizId, principal);
        logger.info("Quiz  has been deleted.");
        return ResponseEntity.ok("Quiz deleted");
    }

    @PutMapping("updateQuiz/{quizId}")
    public ResponseEntity<?> updateYourQuiz(@PathVariable Long quizId, @RequestBody Quiz quiz, Principal principal) {
        quizService.updateQuizById(quizId, quiz, principal);
        logger.info("Quiz  updated.");
        return ResponseEntity.ok("Quiz updated");
    }

    @PutMapping("updateQuizQuestion/{quizId}/{questionId}")
    public ResponseEntity<?> updateYourQuizQuestions(@PathVariable Long quizId, @PathVariable Long questionId, @RequestBody Question questions, Principal principal) {
        quizService.updateQuestion(quizId, questionId, questions, principal);
        logger.info("Question  updated.");
        return ResponseEntity.ok("Question updated");
    }

    @PostMapping("solveQuiz/{quizId}")
    public ResponseEntity<?> solveQuiz(@PathVariable Long quizId, @RequestBody List<Answer> userAnswers, Principal principal) {
        double score = quizService.solveQuiz(quizId, userAnswers, principal);
        return ResponseEntity.ok("Quiz solved successfully. Your score: " + score);
    }
}
