package com.example.quiz_world.quiz.service;

import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.dto.QuestionDTO;
import com.example.quiz_world.quiz.entity.AnswerToQuiz;
import com.example.quiz_world.quiz.entity.Question;
import com.example.quiz_world.quiz.entity.Quiz;
import com.example.quiz_world.quiz.repository.QuizQuestionRepository;
import com.example.quiz_world.quiz.repository.QuizRepository;
import com.example.quiz_world.user.entity.Status;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class QuizQuestionService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final MapperEntity mapperEntity;

    public QuizQuestionService(QuizRepository quizRepository, UserRepository userRepository,
                               QuizQuestionRepository quizQuestionRepository, MapperEntity mapperEntity) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.mapperEntity = mapperEntity;
    }

    public void addQuestionsToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        boolean questionNumberExists = quiz.getQuestions().stream()
                .anyMatch(q -> q.getQuestionNumber().equals(question.getQuestionNumber()));

        if (questionNumberExists) {
            throw new IllegalArgumentException("Question number " + question.getQuestionNumber() + " already exists in this quiz");
        }
        question.setQuiz(quiz);
        question.getAnswerToQuiz().forEach(answer -> answer.setQuestion(question));
        quiz.getQuestions().add(question);

        quizRepository.save(quiz);
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
        return mapperEntity.mapQuestionsToQuestionsDTO(questionsList);
    }

    public void deleteQuestionByNumberQuestionForUser(Long quizId, Long numberQuestion, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz nor found"));
        if (!quiz.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        Question question = quizQuestionRepository.findByQuizIdAndQuestionNumber(quizId, numberQuestion).orElseThrow(() -> new EntityNotFoundException("Question not found"));

        quizQuestionRepository.delete(question);
    }

    public Question updateQuestionByQuestionNumberForUser(Long quizId, Long questionNumber, Question
            question, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        if (!quiz.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this quiz");
        }
        Question questionToUpdate = quizQuestionRepository.findByQuizIdAndQuestionNumber(quizId, questionNumber).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswerToQuiz().clear();

        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            answerToQuiz.setQuestion(questionToUpdate);
            questionToUpdate.getAnswerToQuiz().add(answerToQuiz);
        }
        return quizQuestionRepository.save(questionToUpdate);
    }

    public Question updateQuestionByQuestionNumberForAdmin(Long quizId, Long questionNumber, Question question) {
        Question questionToUpdate = quizQuestionRepository.findByQuizIdAndQuestionNumber(quizId, questionNumber).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswerToQuiz().clear();

        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            answerToQuiz.setQuestion(questionToUpdate);
            questionToUpdate.getAnswerToQuiz().add(answerToQuiz);
        }
        return quizQuestionRepository.save(questionToUpdate);
    }

    public void deleteQuestionByNumberQuestionForAdmin(Long quizId, Long numberQuestion) {
        Question question = quizQuestionRepository.findByQuizIdAndQuestionNumber(quizId, numberQuestion).orElseThrow(() -> new EntityNotFoundException("Question not found"));

        quizQuestionRepository.delete(question);
    }
}
