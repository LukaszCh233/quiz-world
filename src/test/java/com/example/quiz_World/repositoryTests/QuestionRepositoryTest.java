package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.repository.QuestionRepository;
import com.example.quiz_World.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuizRepository quizRepository;

    @BeforeEach
    public void setUp() {
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void shouldSaveQuestion_Test() {
        //Given
        Question question = new Question(null, 1L, "test", null, null);

        //When
        questionRepository.save(question);

        //Then
        List<Question> questionList = questionRepository.findAll();

        assertEquals(1, questionList.size());
        assertFalse(questionList.isEmpty());
        assertEquals(questionList.get(0).getId(), question.getId());
        assertEquals(questionList.get(0).getContent(), question.getContent());
        assertEquals(questionList.get(0).getQuestionNumber(), question.getQuestionNumber());
    }

    @Test
    void shouldDeleteQuestion_Test() {
        //Given
        Question question = new Question(null, 1L, "test", null, null);
        questionRepository.save(question);

        //When
        questionRepository.delete(question);

        //Then
        List<Question> questionList = questionRepository.findAll();

        assertTrue(questionList.isEmpty());
    }

    @Test
    void shouldFindQuestionByQuizIdAndQuestionNumber_Test() {
        //Given
        Quiz quiz = new Quiz(null, null, null, null, null, null);
        quizRepository.save(quiz);

        Question question = new Question(null, 1L, "test", quiz, null);
        questionRepository.save(question);

        //When
        Optional<Question> optionalQuestion = questionRepository.findByQuizIdAndQuestionNumber(quiz.getId(), question.getQuestionNumber());
        assertTrue(optionalQuestion.isPresent());
        Question findQuestion = optionalQuestion.get();

        //Then
        assertEquals(findQuestion.getQuestionNumber(), question.getQuestionNumber());
        assertEquals(findQuestion.getContent(), question.getContent());
        assertEquals(findQuestion.getId(), question.getId());
    }
}
