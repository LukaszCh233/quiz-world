package com.example.quiz_world.repositoryTests;

import com.example.quiz_world.entities.quizEntity.QuizCategory;
import com.example.quiz_world.repository.quiz.QuizCategoryRepository;
import com.example.quiz_world.repository.quiz.QuizRepository;
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
public class QuizCategoryRepositoryTest {
    @Autowired
    QuizCategoryRepository quizCategoryRepository;
    @Autowired
    QuizRepository quizRepository;

    @BeforeEach
    public void setUp() {
        quizRepository.deleteAll();
        quizCategoryRepository.deleteAll();

    }

    @Test
    void shouldSaveQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");

        //When
        quizCategoryRepository.save(quizCategory);

        //Then
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();

        assertFalse(quizCategoryList.isEmpty());
        assertEquals(1, quizCategoryList.size());
        assertEquals(quizCategoryList.get(0).getId(), quizCategory.getId());
        assertEquals(quizCategoryList.get(0).getName(), quizCategory.getName());
    }

    @Test
    void shouldFindAllQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "testCategory");
        QuizCategory quizCategory1 = new QuizCategory(null, "testCategory1");
        quizCategoryRepository.save(quizCategory);
        quizCategoryRepository.save(quizCategory1);

        //When
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();

        //Then
        assertEquals(2, quizCategoryList.size());
        assertEquals(quizCategoryList.get(0).getId(), quizCategory.getId());
        assertEquals(quizCategoryList.get(0).getName(), quizCategory.getName());
        assertEquals(quizCategoryList.get(1).getId(), quizCategory1.getId());
        assertEquals(quizCategoryList.get(1).getName(), quizCategory1.getName());
    }

    @Test
    void shouldFindCategoryById_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");
        quizCategoryRepository.save(quizCategory);

        //When
        Optional<QuizCategory> foundCategory = quizCategoryRepository.findById(quizCategory.getId());

        //Then
        assertTrue(foundCategory.isPresent());
        assertEquals(foundCategory.get().getName(), quizCategory.getName());
        assertEquals(foundCategory.get().getId(), quizCategory.getId());
    }

    @Test
    void shouldDeleteQuizCategory_Test() {
        //Given
        QuizCategory quizCategory = new QuizCategory(null, "test");
        quizCategoryRepository.save(quizCategory);

        //When
        quizCategoryRepository.delete(quizCategory);

        //Then
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();

        assertTrue(quizCategoryList.isEmpty());
    }
}