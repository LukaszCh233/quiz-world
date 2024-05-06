package com.example.quiz_World.repositoryTests;

import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.repository.QuizCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("quizWorldTest")
public class QuizCategoryRepositoryTest {
    @Autowired
    QuizCategoryRepository quizCategoryRepository;

    @BeforeEach
    public void setUp() {
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
        assertTrue(quizCategoryList.contains(quizCategory));
        assertTrue(quizCategoryList.contains(quizCategory1));
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
