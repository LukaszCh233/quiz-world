package com.example.quiz_World.serviceTests;

import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.entities.quizEntity.QuizCategoryDTO;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategoryDTO;
import com.example.quiz_World.repository.QuizCategoryRepository;
import com.example.quiz_World.repository.WordSetCategoryRepository;
import com.example.quiz_World.service.CategoryServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTest {
    private final CategoryServiceImp categoryService;
    private final QuizCategoryRepository quizCategoryRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;

    @Autowired
    public CategoryServiceTest(CategoryServiceImp categoryService, QuizCategoryRepository quizCategoryRepository,
                               WordSetCategoryRepository wordSetCategoryRepository) {
        this.categoryService = categoryService;
        this.quizCategoryRepository = quizCategoryRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
    }

    @BeforeEach
    void setUp() {
        quizCategoryRepository.deleteAll();
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    void shouldCreateQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");

        //When
        QuizCategory createCategory = categoryService.createQuizCategory(quizCategory);

        //Then
        assertNotNull(createCategory);
        assertEquals(quizCategory.getId(), createCategory.getId());
        assertEquals(quizCategory.getName(), createCategory.getName());
    }

    @Test
    void shouldFindAllQuizCategories_Test() {
        //Given
        QuizCategory category1 = new QuizCategory(null, "Category1");
        QuizCategory category2 = new QuizCategory(null, "Category2");
        List<QuizCategory> categories = Arrays.asList(category1, category2);
        quizCategoryRepository.saveAll(categories);

        //When
        List<QuizCategoryDTO> quizCategoryList = categoryService.findAllQuizCategories();

        //Then
        assertEquals(2, quizCategoryList.size());
        assertEquals(categories.get(0).getName(), quizCategoryList.get(0).getName());
        assertEquals(categories.get(1).getName(), quizCategoryList.get(1).getName());
    }

    @Test
    void shouldDeleteQuizCategoryById_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");
        quizCategoryRepository.save(quizCategory);

        //When
        categoryService.deleteQuizCategoryById(quizCategory.getId());

        //Then
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();
        assertTrue(quizCategoryList.isEmpty());
    }

    @Test
    void shouldDeleteAllQuizCategories_Test() {
        //Given
        QuizCategory category1 = new QuizCategory(null, "Category1");
        QuizCategory category2 = new QuizCategory(null, "Category2");
        List<QuizCategory> categories = Arrays.asList(category1, category2);
        quizCategoryRepository.saveAll(categories);

        //When
        categoryService.deleteAllQuizCategories();

        //Then
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();

        assertTrue(quizCategoryList.isEmpty());
    }

    @Test
    void shouldCreateWordSetCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");

        //When
        WordSetCategory createCategory = categoryService.createWordSetCategory(wordSetCategory);

        //Then
        assertNotNull(createCategory);
        assertEquals(wordSetCategory.getId(), createCategory.getId());
        assertEquals(wordSetCategory.getName(), createCategory.getName());
    }

    @Test
    void shouldFindAllWordSetCategories_Test() {
        //Given
        WordSetCategory category1 = new WordSetCategory(null, "Category1");
        WordSetCategory category2 = new WordSetCategory(null, "Category2");
        List<WordSetCategory> categories = Arrays.asList(category1, category2);
        wordSetCategoryRepository.saveAll(categories);

        //When
        List<WordSetCategoryDTO> wordSetCategoryList = categoryService.findAllWordSetCategories();

        //Then
        assertEquals(2, wordSetCategoryList.size());
        assertEquals(categories.get(0).getName(), wordSetCategoryList.get(0).getName());
        assertEquals(categories.get(1).getName(), wordSetCategoryList.get(1).getName());
    }

    @Test
    void shouldDeleteWordSetCategoryById_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");
        wordSetCategoryRepository.save(wordSetCategory);

        //When
        categoryService.deleteWordSetCategoryById(wordSetCategory.getId());

        //Then
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();
        assertTrue(wordSetCategoryList.isEmpty());
    }

    @Test
    void shouldDeleteAllWordSetCategories_Test() {
        //Given
        WordSetCategory category1 = new WordSetCategory(null, "Category1");
        WordSetCategory category2 = new WordSetCategory(null, "Category2");
        List<WordSetCategory> categories = Arrays.asList(category1, category2);
        wordSetCategoryRepository.saveAll(categories);

        //When
        categoryService.deleteAllWordSetCategories();

        //Then
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();
        assertTrue(wordSetCategoryList.isEmpty());
    }
}
