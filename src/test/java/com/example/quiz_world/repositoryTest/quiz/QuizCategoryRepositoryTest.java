package com.example.quiz_world.repositoryTest.quiz;

import com.example.quiz_world.quiz.entity.QuizCategory;
import com.example.quiz_world.quiz.repository.QuizCategoryRepository;
import com.example.quiz_world.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class QuizCategoryRepositoryTest {
    @Autowired
    QuizCategoryRepository quizCategoryRepository;
    @Autowired
    private QuizRepository quizRepository;

    @BeforeEach
    public void setUp() {
        quizRepository.deleteAll();
        quizCategoryRepository.deleteAll();
    }

    @Test
    public void findQuizCategoryByNameIgnoreCase_test() {
        newCategory("TEST");

        Optional<QuizCategory> foundQuizCategory = quizCategoryRepository.findByNameIgnoreCase("test");

        assertTrue(foundQuizCategory.isPresent());
        assertEquals(foundQuizCategory.get().getName(), "TEST");
    }

    private void newCategory(String name) {
        QuizCategory quizCategory = new QuizCategory(null, name);
        quizCategoryRepository.save(quizCategory);
    }
}
