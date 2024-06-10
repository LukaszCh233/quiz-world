package com.example.quiz_World.controllerTests;

import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.service.category.QuizCategoryService;
import com.example.quiz_World.service.category.WordSetCategoryService;
import com.example.quiz_World.service.quiz.QuizQuestionService;
import com.example.quiz_World.service.quiz.QuizService;
import com.example.quiz_World.service.words.WordSetService;
import com.example.quiz_World.service.words.WordsService;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {
    @MockBean
    WordSetCategoryService wordSetCategoryService;
    @MockBean
    QuizService quizService;
    @MockBean
    QuizQuestionService quizQuestionService;
    @MockBean
    WordsService wordService;
    @MockBean
    WordSetService wordSetService;
    @MockBean
    QuizCategoryService quizCategoryService;
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

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public @interface WithMockAdmin {
    }

    @WithMockAdmin
    @Test
    void shouldDeleteQuizzes_Test() throws Exception {
        doNothing().when(quizService).deleteAllQuizzesForAdmin();

        mockMvc.perform(delete("/admin/quizzes"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Quizzes have been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteWordSets_Test() throws Exception {
        doNothing().when(wordSetService).deleteAllWordSetsForAdmin();

        mockMvc.perform(delete("/admin/wordSets"))
                .andExpect(status().isOk())
                .andExpect(content().string("All word sets has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteQuiz_Test() throws Exception {
        Quiz quiz = new Quiz(1L, null, null, null, null, null);

        doNothing().when(quizService).deleteQuizByIdForAdmin(quiz.getId());

        mockMvc.perform(delete("/admin/quiz/{quizId}", quiz.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz have been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteWordSet_Test() throws Exception {
        WordSet wordSet = new WordSet(1L, null, null, null, null, null);

        doNothing().when(wordSetService).deleteWordSetByIdForAdmin(wordSet.getId());

        mockMvc.perform(delete("/admin/wordSet/{wordSetId}", wordSet.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Word set has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldCreateQuizCategory_Test() throws Exception {
        QuizCategory quizCategory = new QuizCategory(null, "test");

        when(quizCategoryService.createQuizCategory(quizCategory)).thenReturn(quizCategory);

        mockMvc.perform(post("/admin/quiz-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @WithMockAdmin
    @Test
    void shouldCreateWordSetCategory_Test() throws Exception {
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");

        when(wordSetCategoryService.createWordSetCategory(wordSetCategory)).thenReturn(wordSetCategory);

        mockMvc.perform(post("/admin/wordSet-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteQuizCategory_Test() throws Exception {
        QuizCategory quizCategory = new QuizCategory(1L, "test");

        doNothing().when(quizCategoryService).deleteQuizCategoryById(quizCategory.getId());

        mockMvc.perform(delete("/admin/quiz-category/{categoryId}", quizCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Category has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteWordSetCategory_Test() throws Exception {
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "test");

        doNothing().when(wordSetCategoryService).deleteWordSetCategoryById(wordSetCategory.getId());

        mockMvc.perform(delete("/admin/wordSet-category/{categoryId}", wordSetCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Category has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateWordSetCategory_Test() throws Exception {
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "test");
        WordSetCategory updateCategory = new WordSetCategory(null, "updateTest");

        when(wordSetCategoryService.updateWordSetCategory(wordSetCategory.getId(), updateCategory)).thenReturn(updateCategory);

        mockMvc.perform(put("/admin/wordSet-category/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldUpdateQuizCategory_Test() throws Exception {
        QuizCategory quizCategory = new QuizCategory(1L, "test");
        QuizCategory updateCategory = new QuizCategory(null, "updateTest");

        when(quizCategoryService.updateQuizCategory(quizCategory.getId(), updateCategory)).thenReturn(updateCategory);

        mockMvc.perform(put("/admin/quiz-category/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldDeleteAllWordSetCategory_Test() throws Exception {
        doNothing().when(wordSetCategoryService).deleteAllWordSetCategories();

        mockMvc.perform(delete("/admin/wordSet-categories"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Categories has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteAllQuizCategory_Test() throws Exception {
        doNothing().when(quizCategoryService).deleteAllQuizCategories();

        mockMvc.perform(delete("/admin/quiz-categories"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Categories has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateQuiz_Test() throws Exception {
        Quiz quiz = new Quiz(1L, null, "test", null, null, null);
        Quiz updateQuiz = new Quiz(null, null, "updateTest", null, null, null);

        when(quizService.updateQuizByIdForAdmin(quiz.getId(), updateQuiz)).thenReturn(updateQuiz);

        mockMvc.perform(put("/admin/quiz/{quizId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldUpdateWordSet_Test() throws Exception {
        WordSet wordSet = new WordSet(1L, "test", null, null, null, null);
        WordSet updateWordSet = new WordSet(null, "updateTest", null, null, null, null);

        when(wordSetService.updateWordSetByIdForAdmin(wordSet.getId(), updateWordSet)).thenReturn(updateWordSet);

        mockMvc.perform(put("/admin/wordSet/{wordSetId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"updateTest\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldDeleteQuestion_Test() throws Exception {
        Long quizId = 1L;
        Long questionNumber = 1L;

        doNothing().when(quizQuestionService).deleteQuestionByNumberQuestionForAdmin(quizId, questionNumber);

        mockMvc.perform(delete("/admin/quiz/{quizId}/question/{questionNumber}", quizId, questionNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Question has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateQuizQuestion() throws Exception {
        Quiz quiz = new Quiz(1L, null, null, null, null, null);
        Question question = new Question(1L, 1L, "test", quiz, null);
        Question updateQuestion = new Question(null, 1L, "updateTest", quiz, null);

        when(quizQuestionService.updateQuestionByQuestionNumberForAdmin(quiz.getId(), question.getQuestionNumber(), updateQuestion)).thenReturn(updateQuestion);

        mockMvc.perform(put("/admin/quiz/{quizId}/question/{questionNumber}", quiz.getId(), question.getQuestionNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"updateTest\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Question updated"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteWord_Test() throws Exception {
        Long wordSetId = 1L;
        Long wordNumber = 1L;

        doNothing().when(wordService).deleteWordByNumberWordSetForAdmin(wordSetId, wordNumber);

        mockMvc.perform(delete("/admin/wordSet/{wordSetId}/word/{wordNumber}", wordSetId, wordNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Word has been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateWord_Test() throws Exception {
        WordSet wordSet = new WordSet(1L, null, null, null, null, null);

        Word word = new Word(1L, 1L, "test", "translation", wordSet);
        Word wordUpdate = new Word(null, 1L, "updateTest", "updateTranslation", wordSet);

        when(wordService.updateWordForAdmin(wordSet.getId(), word.getWordNumber(), wordUpdate)).thenReturn(wordUpdate);

        mockMvc.perform(put("/admin/wordSet/{wordSetId}/word/{wordNumber}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"updateTest\"}")
                        .content("{\"translation\":\"updateTranslation\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}




