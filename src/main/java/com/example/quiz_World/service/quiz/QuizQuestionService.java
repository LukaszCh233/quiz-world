package com.example.quiz_World.service.quiz;

import com.example.quiz_World.dto.QuestionDTO;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.AnswerToQuiz;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.mapper.MapEntity;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.repository.quiz.QuestionRepository;
import com.example.quiz_World.repository.quiz.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class QuizQuestionService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final MapEntity mapEntity;

    public QuizQuestionService(QuizRepository quizRepository, UserRepository userRepository,
                               QuestionRepository questionRepository, MapEntity mapEntity) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.mapEntity = mapEntity;
    }

    public void addQuestionsToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

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
        return mapEntity.mapQuestionsToQuestionsDTO(questionsList);
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

    public Question updateQuestionByQuestionNumberForUser(Long quizId, Long questionNumber, Question
            question, Principal principal) {
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
        return questionRepository.save(questionToUpdate);
    }

    public Question updateQuestionByQuestionNumberForAdmin(Long quizId, Long questionNumber, Question question) {
        Question questionToUpdate = questionRepository.findByQuizIdAndQuestionNumber(quizId, questionNumber).orElseThrow(() -> new EntityNotFoundException("Not found"));

        questionToUpdate.setContent(question.getContent());

        questionToUpdate.getAnswerToQuiz().clear();

        for (AnswerToQuiz answerToQuiz : question.getAnswerToQuiz()) {
            answerToQuiz.setQuestion(questionToUpdate);
            questionToUpdate.getAnswerToQuiz().add(answerToQuiz);
        }
        return questionRepository.save(questionToUpdate);
    }

    public void deleteQuestionByNumberQuestionForAdmin(Long quizId, Long numberQuestion) {
        Question question = questionRepository.findByQuizIdAndQuestionNumber(quizId, numberQuestion).orElseThrow(() -> new EntityNotFoundException("Question not found"));

        questionRepository.delete(question);
    }
}
