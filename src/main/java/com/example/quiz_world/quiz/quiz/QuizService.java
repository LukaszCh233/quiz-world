package com.example.quiz_world.quiz.quiz;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.question.AnswerToQuiz;
import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.quizCategory.QuizCategory;
import com.example.quiz_world.quiz.quizCategory.QuizCategoryRepository;
import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.quiz.reslult.ResultRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

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

    public QuizDTO createQuiz(QuizRequest quizRequest, Principal principal) {
        String email = principal.getName();

        QuizCategory category = quizCategoryRepository.findById(quizRequest.getQuizCategoryId()).orElseThrow(() ->
                new EntityNotFoundException("Category not found"));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(quizRequest.getTitle());
        quiz.setQuizCategory(category);
        quiz.setUserId(user.getId());
        quiz.setStatus(quizRequest.getStatus());

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

    public List<QuizDTO> findQuizzesByUserPrincipal(Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Quiz> quizList = quizRepository.findByUserId(user.getId());
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("Quizzes not found");
        }
        return mapperEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    public QuizDTO findQuizById(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (quiz.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Quiz is private and not accessible");
        }
        return mapperEntity.mapQuizToQuizDTO(quiz);
    }

    public List<QuizDTO> findQuizByCategory(Long categoryId) {
        List<Quiz> quizList = quizRepository.findByQuizCategoryIdAndStatus(categoryId, Status.PUBLIC);
        if (quizList.isEmpty()) {
            throw new EntityNotFoundException("No public quizzes found for this category");
        }
        return mapperEntity.mapQuizzesToQuizzesDTO(quizList);
    }

    @Transactional
    public void deleteAllQuizzesForUser(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        quizRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public void deleteQuizByIdForUser(Long id, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Quiz quizToDelete = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (!quizToDelete.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }

        quizRepository.delete(quizToDelete);
    }

    public void updateQuizByIdForUser(Long quizId, QuizRequest quizRequest, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quizToUpdate = quizRepository.findById(quizId).orElseThrow(() ->
                new EntityNotFoundException("Quiz not found"));
        if (!quizToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to update this quiz");
        }
        updateQuiz(quizToUpdate, quizRequest);

        quizRepository.save(quizToUpdate);
    }

    public void updateQuizByIdForAdmin(Long id, QuizRequest quizRequest) {
        Quiz quizToUpdate = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        updateQuiz(quizToUpdate, quizRequest);

        quizRepository.save(quizToUpdate);
    }

    private void updateQuiz(Quiz quizToUpdate, QuizRequest quizRequest) {
        QuizCategory quizCategory = quizCategoryRepository.findById(quizRequest.getQuizCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not exists"));

        quizToUpdate.setTitle(quizRequest.getTitle());
        quizToUpdate.setQuizCategory(quizCategory);
        quizToUpdate.setStatus(quizRequest.getStatus());
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
        quizRepository.deleteAll();
    }

    @Transactional
    public void deleteQuizByIdForAdmin(Long id) {
        Quiz quizToDelete = quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        quizRepository.delete(quizToDelete);
    }
}
