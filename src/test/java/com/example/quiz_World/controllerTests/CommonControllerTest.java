package com.example.quiz_World.controllerTests;

import com.example.quiz_World.dto.*;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.quizEntity.Question;
import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.mapper.MapEntity;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommonControllerTest {
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
    @MockBean
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
    void shouldDisplayQuizzes_Test() throws Exception {
        List<Quiz> quizzes = List.of(new Quiz(1L, 1L, "quiz", new QuizCategory(2L, "category"), null, Status.PUBLIC));

        when(quizService.findAllPublicQuizzes()).thenReturn(mapEntity.mapQuizzesToQuizzesDTO(quizzes));

        mockMvc.perform(get("/common/quizzes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuiz_Test() throws Exception {
        Long quizId = 1L;
        QuizDTO quizDTO = new QuizDTO(null,"quiz","category");


        when(quizService.findQuizById(quizId)).thenReturn(quizDTO);

        mockMvc.perform(get("/common/quiz/{id}", quizId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("quiz"))
                .andExpect(jsonPath("$.category").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayQuestions_Test() throws Exception {
        Long quizId = 1L;
        QuestionDTO questionDTO = new QuestionDTO(1L,"question",null);

        List<QuestionDTO> questionDTOS = List.of(questionDTO);

        when(quizQuestionService.findQuestionsByQuizId(quizId)).thenReturn(questionDTOS);

        mockMvc.perform(get("/common/quiz/{quizId}/questions", quizId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionNumber").value(1))
                .andExpect(jsonPath("$[0].questionContent").value("question"));
    }

    @WithMockUser
    @Test
    void shouldCreateQuiz_Test() throws Exception {
        Principal principal = mock(Principal.class);

        QuizCategory quizCategory = new QuizCategory(1L, "category");
        QuizDTO quizDTO = new QuizDTO(1L,"quiz","category");

        String jsonContent = "{\"title\":\"test\", \"quizCategory\":{\"id\":1}, \"status\":\"PUBLIC\"}";


        when(quizService.createQuiz(quizDTO.title(), quizCategory.getId(), Status.PUBLIC, principal)).thenReturn(quizDTO);
        mockMvc.perform(post("/common/quiz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayYourQuizzes_Test() throws Exception {
        Principal principal = mock(Principal.class);

        QuizDTO quizDTO = new QuizDTO(null,"quiz","category");

        List<QuizDTO> quizDTOS = List.of(quizDTO);

        when(quizService.findYourQuizzes(principal)).thenReturn(quizDTOS);

        mockMvc.perform(get("/common/user-quizzes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuizByCategory_Test() throws Exception {
        QuizCategory quizCategory = new QuizCategory(1L, "category");
        QuizDTO quizDTO = new QuizDTO(null,"quiz","category");

        List<QuizDTO> quizDTOS = List.of(quizDTO);

        when(quizService.findQuizByCategory(quizCategory.getId())).thenReturn(quizDTOS);

        mockMvc.perform(get("/common/quizzes/category/{categoryId}", quizCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuizCategories_Test() throws Exception {
        QuizCategoryDTO quizCategoryDTO = new QuizCategoryDTO("category");

        List<QuizCategoryDTO> quizCategoryDTOS = List.of(quizCategoryDTO);

        when(quizCategoryService.findAllQuizCategories()).thenReturn(quizCategoryDTOS);
        mockMvc.perform(get("/common/quizCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSetCategories_Test() throws Exception {
        WordSetCategoryDTO wordSetCategoryDTO = new WordSetCategoryDTO("category");

        List<WordSetCategoryDTO> wordSetCategoryDTOList = List.of(wordSetCategoryDTO);

        when(wordSetCategoryService.findAllWordSetCategories()).thenReturn(wordSetCategoryDTOList);
        mockMvc.perform(get("/common/wordSetCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSet_Test() throws Exception {
        Long wordSetId = 1L;
        WordSetDTO wordSetDTO = new WordSetDTO(null,"wordSet","category");

        when(wordSetService.findWordSetById(wordSetId)).thenReturn(wordSetDTO);

        mockMvc.perform(get("/common/wordSet/{id}", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("wordSet"))
                .andExpect(jsonPath("$.category").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSets_Test() throws Exception {
        WordSetDTO wordSetDTO = new WordSetDTO(null,"wordSet","category");

        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);
        when(wordSetService.findPublicWordSets()).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/wordSets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayYourWordSets_Test() throws Exception {
        Principal principal = mock(Principal.class);

        WordSetDTO wordSetDTO = new WordSetDTO(null,"wordSet","category");

        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);

        when(wordSetService.findYourWordSets(principal)).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/user-WordSets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldCreateWordSet_Test() throws Exception {
        Principal principal = mock(Principal.class);
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "category");
        WordSetDTO wordSetDTO = new WordSetDTO(1L,"wordSet","category");

        String jsonContent = "{\"title\":\"test\", \"wordSetCategory\":{\"id\":1}, \"status\":\"PUBLIC\"}";

        WordSet wordSet = new WordSet(1L, "test", null, null, wordSetCategory, Status.PUBLIC);

        when(wordSetService.createWordSet(wordSet.getTitle(), wordSetCategory.getId(), Status.PUBLIC, principal)).thenReturn(wordSetDTO);
        mockMvc.perform(post("/common/wordSet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSetByCategory_Test() throws Exception {
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "category");
        WordSetDTO wordSetDTO = new WordSetDTO(null,"wordSet","category");

        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);

        when(wordSetService.findWordSetByCategory(wordSetCategory.getId())).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/wordSets/category{categoryId}", wordSetCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayWords_Test() throws Exception {
        Long wordSetId = 1L;
        WordDTO wordDTO = new WordDTO(1L,"word");

        List<WordDTO> wordDTOList = List.of(wordDTO);

        when(wordService.findWordsByWordSetId(wordSetId)).thenReturn(wordDTOList);

        mockMvc.perform(get("/common/wordSet/{wordSetId}/words", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].wordNumber").value(1))
                .andExpect(jsonPath("$[0].word").value("word"));
    }

    @WithMockUser
    @Test
    void shouldAddQuestionToQuiz_Test() throws Exception {
        Long quizId = 1L;
        Question question = new Question(1L, 1L, "test", null, new ArrayList<>());

        String questionJson = "{ \"id\": 1, \"content\": \"test\", \"answers\": [] }";

        doNothing().when(quizQuestionService).addQuestionsToQuiz(quizId, question);

        mockMvc.perform(post("/common/quiz/{quizId}/question", quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Question added to quiz successfully"));
    }

    @WithMockUser
    @Test
    void shouldAddWordToWordSet_Test() throws Exception {
        Long wordSetId = 1L;
        Word word = new Word(1L, 1L, "test", "test", null);

        String wordJson = "{ \"id\": 1, \"word\": \"test\", \"translation\": \"test\" }";

        doNothing().when(wordService).addWordToWordSet(wordSetId, word);

        mockMvc.perform(post("/common/wordSet/{wordSetId}/word", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wordJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Word added to word set successfully"));
    }
}
