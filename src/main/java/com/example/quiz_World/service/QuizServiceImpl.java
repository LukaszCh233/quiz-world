package com.example.quiz_World.service;

import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.ResultDTO;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;
    private final MapEntity mapEntity;

    public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository,
                           QuizCategoryRepository quizCategoryRepository, UserRepository userRepository,
                           ResultRepository resultRepository, MapEntity mapEntity) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.quizCategoryRepository = quizCategoryRepository;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.mapEntity = mapEntity;
    }

    public QuizDTO createQuiz(String title, QuizCategory quizCategory, Status status, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuizCategory(quizCategory);
        quiz.setUserId(user.getId());
        quiz.setStatus(status);

        quizRepository.save(quiz);
        return mapEntity.mapQuizToQuizDTO(quiz);
    }

    public void addQuestionsToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        question.setQuiz(quiz);
        question.getAnswerToQuiz().forEach(answer -> answer.setQuestion(question));
        quiz.getQuestions().add(question);

        quizRepository.save(quiz);
    }

    public List<QuizDTO> findAllPublicQuizzes() {
        List<Quiz> quizList = quizRepository.findByStatus(Status.PUBLIC);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public List<QuizDTO> findYourQuizzes(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Quiz> quizList = quizRepository.findByUserId(user.getId());
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Not found quizzes");
        }
        return mapEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public QuizDTO findQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (quiz.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Quiz not found");
        }
        return mapEntity.mapQuizToQuizDTO(quiz);
    }

    public List<QuizDTO> findQuizByCategoryId(Long categoryId) {
        List<Quiz> quizList = quizRepository.findByQuizCategoryId(categoryId);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Not found quiz with this category");
        }
        return mapEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public List<QuestionDTO> findQuestionsByQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (quiz.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Quiz not found");
        }
        List<Question> questionsList = quiz.getQuestions();
        if (questionsList.isEmpty()) {
            throw new EntityNotFoundException("Quiz is empty");
        }

        return mapEntity.mapQuestionsToQuestionsDTO(questionsList);
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

    public void deleteQuestionByNumberQuestionForUser(Long quizId, Long numberQuestion, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz nor found"));
        if (!quiz.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        Question question = questionRepository.findByQuizIdAndQuestionNumber(quizId, numberQuestion).orElseThrow(() -> new EntityNotFoundException("Question not found"));

        questionRepository.delete(question);
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

    public void updateQuestionByQuestionNumberForUser(Long quizId, Long questionNumber, Question question, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        if (!quiz.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        Question questionToUpdate = questionRepository.findByQuizIdAndQuestionNumber(quizId, questionNumber).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswerToQuiz().clear();

        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            answerToQuiz.setQuestion(questionToUpdate);
            questionToUpdate.getAnswerToQuiz().add(answerToQuiz);
        }
        questionRepository.save(questionToUpdate);
    }

    public void updateQuestionByQuestionNumberForAdmin(Long quizId, Long questionNumber, Question question) {
        Question questionToUpdate = questionRepository.findByQuizIdAndQuestionNumber(quizId, questionNumber).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswerToQuiz().clear();

        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            answerToQuiz.setQuestion(questionToUpdate);
            questionToUpdate.getAnswerToQuiz().add(answerToQuiz);
        }
        questionRepository.save(questionToUpdate);
    }

    public void updateQuizByIdForAdmin(Long id, Quiz quiz) {
        Quiz quizToUpdate = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        if (quiz.getQuizCategory() == null) {
            throw new IllegalArgumentException("QuizCategory cannot be null");
        }
        QuizCategory quizCategory = quizCategoryRepository.findById(quiz.getQuizCategory().getId()).orElseThrow(() -> new EntityNotFoundException("Quiz category not exists"));

        quizToUpdate.setTitle(quiz.getTitle());
        quizToUpdate.setQuizCategory(quizCategory);
        quizToUpdate.setStatus(quiz.getStatus());

        quizRepository.save(quizToUpdate);
    }

    public void deleteQuestionByNumberQuestionForAdmin(Long quizId, Long numberQuestion) {
        Question question = questionRepository.findByQuizIdAndQuestionNumber(quizId, numberQuestion).orElseThrow(() -> new EntityNotFoundException("Question not found"));

        questionRepository.delete(question);
    }

    public List<Quiz> findQuizzesByUserPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(quizRepository.findByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found quizzes"));
    }

    public double solveQuiz(Long quizId, List<AnswerToQuiz> userAnswerToQuizs, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        List<Question> questions = quiz.getQuestions();
        if (userAnswerToQuizs.size() != questions.size()) {
            throw new IllegalArgumentException("Number of answers user does not match with questions");
        }
        Result previousResult = resultRepository.findByUserIdAndQuizId(user.getId(), quizId);

        int correctAnswersCount = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            AnswerToQuiz userAnswerToQuiz = userAnswerToQuizs.get(i);
            if (isCorrectAnswer(question, userAnswerToQuiz)) {
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

    private boolean isCorrectAnswer(Question question, AnswerToQuiz userAnswerToQuiz) {
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
        return userAnswerToQuiz.getAnswerNumber().equals(correctAnswerToQuiz.getAnswerNumber());

    }

    public List<ResultDTO> findQuizzesResults(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }

        return mapEntity.mapQuizResultsToQuizResultsDTO(results);
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