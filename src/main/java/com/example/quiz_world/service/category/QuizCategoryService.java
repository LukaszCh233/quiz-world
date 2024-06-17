package com.example.quiz_world.service.category;

import com.example.quiz_world.dto.QuizCategoryDTO;
import com.example.quiz_world.entities.quizEntity.QuizCategory;
import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapEntity;
import com.example.quiz_world.repository.quiz.QuizCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizCategoryService {
    private final QuizCategoryRepository quizCategoryRepository;
    private final MapEntity mapEntity;

    public QuizCategoryService(QuizCategoryRepository quizCategoryRepository, MapEntity mapEntity) {
        this.quizCategoryRepository = quizCategoryRepository;
        this.mapEntity = mapEntity;
    }

    public QuizCategory createQuizCategory(QuizCategory quizCategory) {
        Optional<QuizCategory> existingCategory = quizCategoryRepository.findByNameIgnoreCase(quizCategory.getName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Category exists");
        }
        return quizCategoryRepository.save(quizCategory);
    }

    public List<QuizCategoryDTO> findAllQuizCategories() {
        List<QuizCategory> categories = quizCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Quiz category list is empty");
        }
        return mapEntity.mapQuizCategoriesToQuizCategoriesDTO(categories);
    }

    @Transactional
    public void deleteQuizCategoryById(Long categoryId) {
        QuizCategory quizCategory = quizCategoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Quiz category not found"));

        quizCategoryRepository.delete(quizCategory);
    }

    @Transactional
    public void deleteAllQuizCategories() {
        List<QuizCategory> quizCategoryList = quizCategoryRepository.findAll();
        if (quizCategoryList.isEmpty()) {
            throw new EntityNotFoundException("Not found categories in Quiz");
        }
        quizCategoryRepository.deleteAll();
    }

    public QuizCategory updateQuizCategory(Long idCategory, QuizCategory updateCategory) {
        QuizCategory presentCategory = quizCategoryRepository.findById(idCategory).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        presentCategory.setName(updateCategory.getName());
        return quizCategoryRepository.save(presentCategory);
    }
}

