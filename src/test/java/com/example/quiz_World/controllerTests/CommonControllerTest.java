package com.example.quiz_World.controllerTests;

import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.quizEntity.*;
import com.example.quiz_World.entities.wordSetEntity.*;
import com.example.quiz_World.repository.WordSetCategoryRepository;
import com.example.quiz_World.service.CategoryServiceImp;
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
    CategoryServiceImp categoryService;
    @MockBean
    QuizServiceImpl quizService;
    @MockBean
    WordServiceImpl wordService;
    @MockBean
    MapEntity mapEntity;
    @MockBean
    WordSetCategoryRepository wordSetCategoryRepository;
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
        List<Quiz> quizzes = List.of(new Quiz(1L, 1L, "quiz", new QuizCategory(null, "category"), null, Status.PUBLIC));

        when(quizService.findAllPublicQuizzes()).thenReturn(mapEntity.mapQuizzesToQuizzesDTO(quizzes));

        mockMvc.perform(get("/common/quizzes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuiz_Test() throws Exception {
        Long quizId = 1L;
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("quiz");
        quizDTO.setCategory("category");

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
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionNumber(1L);
        questionDTO.setQuestionContent("question");
        List<QuestionDTO> questionDTOS = List.of(questionDTO);

        when(quizService.findQuestionsByQuizId(quizId)).thenReturn(questionDTOS);

        mockMvc.perform(get("/common/questions/{quizId}", quizId)
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
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1L);
        quizDTO.setTitle("quiz");
        quizDTO.setCategory("category");
        String jsonContent = "{\"title\":\"test\", \"quizCategory\":{\"id\":1}, \"status\":\"PUBLIC\"}";


        when(quizService.createQuiz(quizDTO.getTitle(), quizCategory.getId(), Status.PUBLIC, principal)).thenReturn(quizDTO);
        mockMvc.perform(post("/common/createQuiz")
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

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("quiz");
        quizDTO.setCategory("category");
        List<QuizDTO> quizDTOS = List.of(quizDTO);

        when(quizService.findYourQuizzes(principal)).thenReturn(quizDTOS);

        mockMvc.perform(get("/common/yourQuizzes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuizByCategory_Test() throws Exception {
        QuizCategory quizCategory = new QuizCategory(1L, "category");
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("quiz");
        quizDTO.setCategory(quizCategory.getName());
        List<QuizDTO> quizDTOS = List.of(quizDTO);

        when(quizService.findQuizByCategory(quizCategory.getId())).thenReturn(quizDTOS);

        mockMvc.perform(get("/common/quizzes/{categoryId}", quizCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayQuizCategories_Test() throws Exception {
        QuizCategoryDTO quizCategoryDTO = new QuizCategoryDTO();
        quizCategoryDTO.setName("category");
        List<QuizCategoryDTO> quizCategoryDTOS = List.of(quizCategoryDTO);

        when(categoryService.findAllQuizCategories()).thenReturn(quizCategoryDTOS);
        mockMvc.perform(get("/common/quizCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSetCategories_Test() throws Exception {
        WordSetCategoryDTO wordSetCategoryDTO = new WordSetCategoryDTO();
        wordSetCategoryDTO.setName("category");
        List<WordSetCategoryDTO> wordSetCategoryDTOList = List.of(wordSetCategoryDTO);

        when(categoryService.findAllWordSetCategories()).thenReturn(wordSetCategoryDTOList);
        mockMvc.perform(get("/common/wordSetCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSet_Test() throws Exception {
        Long wordSetId = 1L;
        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setTitle("wordSet");
        wordSetDTO.setCategory("category");

        when(wordService.findWordSetById(wordSetId)).thenReturn(wordSetDTO);

        mockMvc.perform(get("/common/wordSet/{id}", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("wordSet"))
                .andExpect(jsonPath("$.category").value("category"));
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSets_Test() throws Exception {
        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setTitle("wordSet");
        wordSetDTO.setCategory("category");
        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);
        when(wordService.findPublicWordSets()).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/wordSets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayYourWordSets_Test() throws Exception {
        Principal principal = mock(Principal.class);

        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setTitle("wordSet");
        wordSetDTO.setCategory("category");
        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);

        when(wordService.findYourWordSets(principal)).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/yourWordSets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldCreateWordSet_Test() throws Exception {
        Principal principal = mock(Principal.class);
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "category");
        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setId(1L);
        wordSetDTO.setTitle("test");
        wordSetDTO.setCategory("category");

        String jsonContent = "{\"title\":\"test\", \"wordSetCategory\":{\"id\":1}, \"status\":\"PUBLIC\"}";


        WordSet wordSet = new WordSet(1L, "test", null, null, wordSetCategory, Status.PUBLIC);


        when(wordService.createWordSet(wordSet.getTitle(), wordSetCategory.getId(), Status.PUBLIC, principal)).thenReturn(wordSetDTO);
        mockMvc.perform(post("/common/createWordSet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayWordSetByCategory_Test() throws Exception {
        WordSetCategory wordSetCategory = new WordSetCategory(1L, "category");
        WordSetDTO wordSetDTO = new WordSetDTO();
        wordSetDTO.setTitle("wordSet");
        wordSetDTO.setCategory("category");
        List<WordSetDTO> wordSetDTOList = List.of(wordSetDTO);

        when(wordService.findWordSetByCategory(wordSetCategory.getId())).thenReturn(wordSetDTOList);

        mockMvc.perform(get("/common/wordSets/{categoryId}", wordSetCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void shouldDisplayWords_Test() throws Exception {
        Long wordSetId = 1L;
        WordDTO wordDTO = new WordDTO();
        wordDTO.setWordNumber(1L);
        wordDTO.setWord("word");
        List<WordDTO> wordDTOList = List.of(wordDTO);

        when(wordService.findWordsByWordSetId(wordSetId)).thenReturn(wordDTOList);

        mockMvc.perform(get("/common/words/{wordSetId}", wordSetId)
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

        doNothing().when(quizService).addQuestionsToQuiz(quizId, question);

        mockMvc.perform(post("/common/addQuestionsToQuiz/{quizId}", quizId)
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

        mockMvc.perform(post("/common/addWordToWordSet/{wordSetId}", wordSetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wordJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Word added to word set successfully"));
    }
}
