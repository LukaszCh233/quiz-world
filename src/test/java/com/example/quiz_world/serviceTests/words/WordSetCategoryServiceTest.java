package com.example.quiz_world.serviceTests.words;

import com.example.quiz_world.dto.WordSetCategoryDTO;
import com.example.quiz_world.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_world.repository.words.WordSetCategoryRepository;
import com.example.quiz_world.service.category.WordSetCategoryService;
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
public class WordSetCategoryServiceTest {
    private final WordSetCategoryService wordSetCategoryService;
    private final WordSetCategoryRepository wordSetCategoryRepository;

    @Autowired
    public WordSetCategoryServiceTest(WordSetCategoryService wordSetCategoryService,
                                      WordSetCategoryRepository wordSetCategoryRepository) {
        this.wordSetCategoryService = wordSetCategoryService;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
    }

    @BeforeEach
    void setUp() {
        wordSetCategoryRepository.deleteAll();
    }

    @Test
    void shouldCreateWordSetCategory_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");

        //When
        WordSetCategory createCategory = wordSetCategoryService.createWordSetCategory(wordSetCategory);

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
        List<WordSetCategoryDTO> wordSetCategoryList = wordSetCategoryService.findAllWordSetCategories();

        //Then
        assertEquals(2, wordSetCategoryList.size());
        assertEquals(categories.get(0).getName(), wordSetCategoryList.get(0).name());
        assertEquals(categories.get(1).getName(), wordSetCategoryList.get(1).name());
    }

    @Test
    void shouldDeleteWordSetCategoryById_Test() {
        //Given
        WordSetCategory wordSetCategory = new WordSetCategory(null, "test");
        wordSetCategoryRepository.save(wordSetCategory);

        //When
        wordSetCategoryService.deleteWordSetCategoryById(wordSetCategory.getId());

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
        wordSetCategoryService.deleteAllWordSetCategories();

        //Then
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();
        assertTrue(wordSetCategoryList.isEmpty());
    }
}
