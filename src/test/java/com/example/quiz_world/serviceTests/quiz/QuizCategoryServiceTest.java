package com.example.quiz_world.serviceTests.quiz;

import com.example.quiz_world.dto.QuizCategoryDTO;
import com.example.quiz_world.entities.quizEntity.QuizCategory;
import com.example.quiz_world.repository.quiz.QuizCategoryRepository;
import com.example.quiz_world.service.category.QuizCategoryService;
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
public class QuizCategoryServiceTest {
    private final QuizCategoryService quizCategoryService;
    private final QuizCategoryRepository quizCategoryRepository;

    @Autowired
    public QuizCategoryServiceTest(QuizCategoryService quizCategoryService, QuizCategoryRepository quizCategoryRepository) {
        this.quizCategoryService = quizCategoryService;
        this.quizCategoryRepository = quizCategoryRepository;
    }

    @BeforeEach
    void setUp() {
        quizCategoryRepository.deleteAll();

    }

    @Test
    void shouldCreateQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");

        //When
        QuizCategory createCategory = quizCategoryService.createQuizCategory(quizCategory);

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
        List<QuizCategoryDTO> quizCategoryList = quizCategoryService.findAllQuizCategories();

        //Then
        assertEquals(2, quizCategoryList.size());
        assertEquals(categories.get(0).getName(), quizCategoryList.get(0).name());
        assertEquals(categories.get(1).getName(), quizCategoryList.get(1).name());
    }

    @Test
    void shouldDeleteQuizCategoryById_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");
        quizCategoryRepository.save(quizCategory);

        //When
        quizCategoryService.deleteQuizCategoryById(quizCategory.getId());

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
        quizCategoryService.deleteAllQuizCategories();

        //Then
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();

        assertTrue(quizCategoryList.isEmpty());
    }
}
