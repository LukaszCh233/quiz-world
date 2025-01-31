package com.example.quiz_world.quiz.quizCategory;

import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.quiz.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizCategoryService {
    private final QuizCategoryRepository quizCategoryRepository;
    private final QuizRepository quizRepository;
    private final MapperEntity mapperEntity;

    public QuizCategoryService(QuizCategoryRepository quizCategoryRepository, QuizRepository quizRepository,
                               MapperEntity mapperEntity) {
        this.quizCategoryRepository = quizCategoryRepository;
        this.quizRepository = quizRepository;
        this.mapperEntity = mapperEntity;
    }

    public QuizCategoryDTO createQuizCategory(QuizCategoryRequest quizCategoryRequest) {
        Optional<QuizCategory> existingCategory = quizCategoryRepository.findByNameIgnoreCase
                (quizCategoryRequest.getCategoryName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Category exists");
        }

        QuizCategory quizCategory = new QuizCategory();
        quizCategory.setName(quizCategoryRequest.getCategoryName());

        quizCategoryRepository.save(quizCategory);

        return mapperEntity.mapQuizCategoryToQuizCategoryDTO(quizCategory);
    }

    public List<QuizCategoryDTO> findAllQuizCategories() {
        List<QuizCategory> categories = quizCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Quiz category list is empty");
        }
        return mapperEntity.mapQuizCategoriesToQuizCategoriesDTO(categories);
    }

    @Transactional
    public void deleteQuizCategoryById(Long categoryId) {
        QuizCategory quizCategory = quizCategoryRepository.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException("Quiz category not found"));

        if (quizRepository.existsByQuizCategoryId(categoryId)) {
            throw new IllegalStateException("Cannot delete category as it is associated with existing quizzes.");
        }

        quizCategoryRepository.delete(quizCategory);
    }

    @Transactional
    public void deleteAllQuizCategories() {
        if (quizRepository.findFirstBy().isPresent()) {
            throw new IllegalArgumentException("Cannot delete categories because there are quizzes associated with them.");
        }
        quizCategoryRepository.deleteAll();
    }

    public QuizCategoryDTO updateQuizCategory(Long idCategory, QuizCategoryRequest quizCategoryRequest) {
        QuizCategory presentCategory = quizCategoryRepository.findById(idCategory).orElseThrow(() ->
                new EntityNotFoundException("Category not found"));
        if (quizCategoryRepository.existsByName(quizCategoryRequest.getCategoryName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        presentCategory.setName(quizCategoryRequest.getCategoryName());

        quizCategoryRepository.save(presentCategory);

        return mapperEntity.mapQuizCategoryToQuizCategoryDTO(presentCategory);
    }
}

