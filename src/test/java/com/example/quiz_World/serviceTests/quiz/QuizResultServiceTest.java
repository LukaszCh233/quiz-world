package com.example.quiz_World.serviceTests.quiz;

import com.example.quiz_World.dto.QuizResultDTO;
import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.repository.quiz.QuizRepository;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.service.quiz.QuizResultService;
import com.example.quiz_World.serviceTests.TestPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
public class QuizResultServiceTest {
    private final QuizRepository quizRepository;
    private final QuizResultService quizResultService;
    private final UserRepository userRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public QuizResultServiceTest(QuizRepository quizRepository, QuizResultService quizResultService,
                                 UserRepository userRepository, ResultRepository resultRepository) {
        this.quizRepository = quizRepository;
        this.quizResultService = quizResultService;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
    }

    @BeforeEach
    public void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void shouldFindQuizzesResults_Test() {
        //Given
        User user = new User(null, "user", "test1", "testPassword", Role.USER);
        userRepository.save(user);

        Quiz quiz = new Quiz(null, null, "old", null, null, Status.PRIVATE);
        quizRepository.save(quiz);

        Result result = new Result(null, user, quiz, null, 100D);
        Result result1 = new Result(null, user, quiz, null, 50D);
        resultRepository.save(result);
        resultRepository.save(result1);

        //When
        List<QuizResultDTO> quizzesResults = quizResultService.findYourQuizzesResults(new TestPrincipal(user.getEmail()));

        //Then
        assertEquals(2, quizzesResults.size());
        assertFalse(quizzesResults.isEmpty());
        assertEquals(result.getScore(), quizzesResults.get(0).score());
        assertEquals(result1.getScore(), quizzesResults.get(1).score());
    }
}
