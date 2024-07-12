package com.example.quiz_world.quiz.service;

import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.dto.QuizDTO;
import com.example.quiz_world.quiz.entity.*;
import com.example.quiz_world.quiz.repository.QuizCategoryRepository;
import com.example.quiz_world.quiz.repository.QuizRepository;
import com.example.quiz_world.reslult.Result;
import com.example.quiz_world.reslult.ResultRepository;
import com.example.quiz_world.user.entity.Status;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final MapperEntity mapperEntity;

    public QuizService(QuizRepository quizRepository, QuizCategoryRepository quizCategoryRepository,
                       UserRepository userRepository, ResultRepository resultRepository, MapperEntity mapperEntity) {
        this.quizRepository = quizRepository;
        this.quizCategoryRepository = quizCategoryRepository;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.mapperEntity = mapperEntity;
    }

    public QuizDTO createQuiz(String title, Long quizCategoryId, Status status, Principal principal) {
        String email = principal.getName();

        QuizCategory category = quizCategoryRepository.findById(quizCategoryId).orElseThrow(() -> new EntityNotFoundException("category not found"));
        Quiz quiz = new Quiz();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));
        quiz.setTitle(title);
        quiz.setQuizCategory(category);
        quiz.setUserId(user.getId());
        quiz.setStatus(status);

        quizRepository.save(quiz);
        return mapperEntity.mapQuizToQuizDTO(quiz);

    }

    public List<QuizDTO> findAllPublicQuizzes() {
        List<Quiz> quizList = quizRepository.findByStatus(Status.PUBLIC);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapperEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public List<QuizDTO> findYourQuizzes(Principal principal) {
        String email = principal.getName();
        List<Quiz> quizList;

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));
        quizList = quizRepository.findByUserId(user.getId());
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Not found Quizzes");
        }
        return mapperEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public QuizDTO findQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (quiz.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Quiz not found");
        }
        return mapperEntity.mapQuizToQuizDTO(quiz);
    }

    public List<QuizDTO> findQuizByCategory(Long categoryId) {
        List<Quiz> quizList = quizRepository.findByQuizCategoryId(categoryId);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Not found quiz with this category");
        }
        return mapperEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    @Transactional
    public void deleteAllQuizzesForUser(Principal principal) {
        List<Quiz> quizList = findQuizzesByUserPrincipal(principal);

        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        for (Quiz quiz : quizList) {
            resultRepository.deleteByQuiz(quiz);
        }
        quizRepository.deleteAll(quizList);
    }

    @Transactional
    public void deleteQuizByIdForUser(Long id, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quizToDelete = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz nor found"));
        if (!quizToDelete.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        resultRepository.deleteByQuiz(quizToDelete);

        quizRepository.delete(quizToDelete);
    }

    public void updateQuizByIdForUser(Long id, Quiz quiz, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quizToUpdate = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (!quizToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        if (quiz.getQuizCategory() == null) {
            throw new IllegalArgumentException("Quiz category cannot be null");
        }
        QuizCategory quizCategory = quizCategoryRepository.findById(quiz.getQuizCategory().getId()).orElseThrow(() -> new EntityNotFoundException("Category not exists"));

        quizToUpdate.setTitle(quiz.getTitle());
        quizToUpdate.setQuizCategory(quizCategory);
        quizToUpdate.setStatus(quiz.getStatus());

         quizRepository.save(quizToUpdate);
    }

    public void updateQuizByIdForAdmin(Long id, Quiz quiz) {
        Quiz quizToUpdate = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        if (quiz.getQuizCategory() == null) {
            throw new IllegalArgumentException("Quiz category cannot be null");
        }
        QuizCategory quizCategory = quizCategoryRepository.findById(quiz.getQuizCategory().getId()).orElseThrow(() -> new EntityNotFoundException("Quiz category not exists"));

        quizToUpdate.setTitle(quiz.getTitle());
        quizToUpdate.setQuizCategory(quizCategory);
        quizToUpdate.setStatus(quiz.getStatus());

         quizRepository.save(quizToUpdate);
    }

    public List<Quiz> findQuizzesByUserPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(quizRepository.findByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found quizzes"));
    }

    public double solveQuiz(Long quizId, List<UserAnswer> userAnswerToQuiz, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();
        if (userAnswerToQuiz.size() != questions.size()) {
            throw new IllegalArgumentException("Number of answers user does not match with questions");
        }
        Result previousResult = resultRepository.findByUserIdAndQuizId(user.getId(), quizId);

        int correctAnswersCount = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            UserAnswer answerToQuiz = userAnswerToQuiz.get(i);
            if (isCorrectAnswer(question, answerToQuiz)) {
                correctAnswersCount++;
            }
        }
        double score = (double) correctAnswersCount / questions.size() * 100;
        if (previousResult == null || score > previousResult.getScore()) {

            Result result = new Result();
            result.setUser(user);
            result.setScore(score);
            result.setQuiz(quiz);
            resultRepository.save(result);
        }
        if (previousResult != null && score > previousResult.getScore()) {
            resultRepository.delete(previousResult);
        }
        return score;
    }

    private boolean isCorrectAnswer(Question question, UserAnswer userAnswerToQuiz) {
        AnswerToQuiz correctAnswerToQuiz = null;
        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            if (answerToQuiz.isCorrect()) {
                correctAnswerToQuiz = answerToQuiz;
                break;
            }
        }
        if (correctAnswerToQuiz == null) {
            return false;
        }
        return userAnswerToQuiz.getAnswer().equals(correctAnswerToQuiz.getAnswerNumber());
    }

    @Transactional
    public void deleteAllQuizzesForAdmin() {
        List<Quiz> quizList = quizRepository.findAll();
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Quiz list is empty");
        }
        for (Quiz quiz : quizList) {
            resultRepository.deleteByQuiz(quiz);
        }
        quizRepository.deleteAll(quizList);
    }

    @Transactional
    public void deleteQuizByIdForAdmin(Long id) {
        Quiz quizToDelete = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        resultRepository.deleteByQuiz(quizToDelete);

        quizRepository.delete(quizToDelete);
    }
}
