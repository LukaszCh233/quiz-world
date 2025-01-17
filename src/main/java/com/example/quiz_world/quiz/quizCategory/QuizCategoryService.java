package com.example.quiz_world.quiz.quizCategory;

import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizCategoryService {
    private final QuizCategoryRepository quizCategoryRepository;
    private final MapperEntity mapperEntity;

    public QuizCategoryService(QuizCategoryRepository quizCategoryRepository, MapperEntity mapperEntity) {
        this.quizCategoryRepository = quizCategoryRepository;
        this.mapperEntity = mapperEntity;
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
        return mapperEntity.mapQuizCategoriesToQuizCategoriesDTO(categories);
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

