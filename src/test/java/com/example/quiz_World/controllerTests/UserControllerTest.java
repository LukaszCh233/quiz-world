package com.example.quiz_World.controllerTests;

import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.Role;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.UserAnswer;
import com.example.quiz_World.entities.wordSetEntity.AnswerToWordSet;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.service.MapEntity;
import com.example.quiz_World.service.QuizServiceImpl;
import com.example.quiz_World.service.WordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("quizWorldTest")
public class UserControllerTest {

    @MockBean
    QuizServiceImpl quizService;
    @MockBean
    WordServiceImpl wordService;
    @Autowired
    MapEntity mapEntity;
    @Autowired
    WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser
    @Test
    void shouldDeleteQuizzes_Test() throws Exception {
        Principal principal = mock(Principal.class);

        doNothing().when(quizService).deleteAllQuizzesForUser(principal);

        mockMvc.perform(delete("/user/deleteQuizzes"))
                .andExpect(status().isOk())
                .andExpect(content().string("All quizzes has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldDeleteQuiz_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Quiz quiz = new Quiz(1L, null, null, null, null, null);

        doNothing().when(quizService).deleteQuizByIdForUser(quiz.getId(), principal);

        mockMvc.perform(delete("/user/deleteQuiz/{quizId}", quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldUpdateQuiz_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Quiz quiz = new Quiz(1L, null, "test", null, null, null);
        Quiz updateQuiz = new Quiz(null, null, "updateTest", null, null, null);

        when(quizService.updateQuizByIdForUser(quiz.getId(), updateQuiz, principal)).thenReturn(updateQuiz);

        mockMvc.perform(put("/user/updateQuiz/{quizId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDeleteWordSet_Test() throws Exception {
        Principal principal = mock(Principal.class);
        Long wordSetId = 1L;

        doNothing().when(wordService).deleteWordSetByIdForUser(wordSetId, principal);

        mockMvc.perform(delete("/user/deleteWordSet/{wordSetId}", wordSetId))
                .andExpect(status().isOk())
                .andExpect(content().string("Word set has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldDeleteWordSets_Test() throws Exception {
        Principal principal = mock(Principal.class);

        doNothing().when(wordService).deleteAllWordSetsForUser(principal);

        mockMvc.perform(delete("/user/deleteWordSets"))
                .andExpect(status().isOk())
                .andExpect(content().string("All word sets has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldUpdateWordSet_Test() throws Exception {
        Principal principal = mock(Principal.class);

        WordSet wordSet = new WordSet(1L, "test", null, null, null, null);
        WordSet updateWordSet = new WordSet(null, "updateTest", null, null, null, null);

        when(wordService.updateWordSetByIdForUser(wordSet.getId(), updateWordSet, principal)).thenReturn(updateWordSet);

        mockMvc.perform(put("/user/updateWordSet/{wordSetId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDeleteQuestion_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Long quizId = 1L;
        Long questionNumber = 1L;

        doNothing().when(quizService).deleteQuestionByNumberQuestionForUser(quizId, questionNumber, principal);

        mockMvc.perform(delete("/user/deleteQuestion/{quizId}/{questionNumber}", quizId, questionNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Question has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldUpdateQuizQuestion() throws Exception {
        Principal principal = mock(Principal.class);

        Long quizId = 1L;
        Question question = new Question(1L, 1L, "test", null, null);
        Question updateQuestion = new Question(null, 1L, "updateTest",null , null);

        when(quizService.updateQuestionByQuestionNumberForUser(quizId, question.getQuestionNumber(), updateQuestion, principal)).thenReturn(updateQuestion);

        mockMvc.perform(put("/user/updateQuizQuestion/{quizId}/{questionNumber}", quizId, question.getQuestionNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"updateTest\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Question updated"));
    }

    @WithMockUser
    @Test
    void shouldDeleteWord_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Long wordSetId = 1L;
        Long wordNumber = 1L;

        doNothing().when(wordService).deleteWordByNumberWordSetForUser(wordSetId, wordNumber, principal);

        mockMvc.perform(delete("/user/deleteWord/{wordSetId}/{wordNumber}", wordSetId, wordNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Word has been deleted"));
    }

    @WithMockUser
    @Test
    void shouldUpdateWord_Test() throws Exception {
        Principal principal = mock(Principal.class);

       Long wordSetId =1L;
        Word word = new Word(1L, 1L, "test", "translation", null);
        Word wordUpdate = new Word(null, 1L, "updateTest", "updateTranslation", null);

        when(wordService.updateWordForUser(wordSetId, word.getWordNumber(), wordUpdate, principal)).thenReturn(wordUpdate);

        mockMvc.perform(put("/user/updateWord/{wordSetId}/{wordNumber}", wordSetId, word.getWordNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"updateTest\"}")
                        .content("{\"translation\":\"updateTranslation\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldSolveQuiz_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Long quizId = 1L;

        List<UserAnswer> userAnswer = List.of(new UserAnswer(1L));
        double expectedScore = 100.0;

        when(quizService.solveQuiz(quizId, userAnswer, principal)).thenReturn(expectedScore);

        mockMvc.perform(post("/user/solveQuiz/{quizId}", quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"answer\":\"1\"}]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuizzesScore_Test() throws Exception {
        Principal principal = mock(Principal.class);

        User user = new User(null, null, null, null, Role.USER);
        Quiz quiz = new Quiz(1L, null, null, null, null, null);
        List<Result> results = List.of(new Result(1L, user, quiz, null, 100.0));

        when(quizService.findQuizzesResults(principal)).thenReturn(mapEntity.mapQuizResultsToQuizResultsDTO(results));

        mockMvc.perform(get("/user/score"))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldSolveWordSet_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Long wordSetId = 1L;
        List<AnswerToWordSet> answers = List.of(new AnswerToWordSet("translation"));
        double expectedScore = 100.0;

        when(wordService.solveWordSet(wordSetId, answers, principal)).thenReturn(expectedScore);

        mockMvc.perform(post("/user/solveWordSet/{wordSetId}", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"answer\":\"translation\"}]")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

