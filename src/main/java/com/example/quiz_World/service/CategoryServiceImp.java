package com.example.quiz_World.service;

import com.example.quiz_World.entities.CategoryQuiz;
import com.example.quiz_World.repository.CategoryQuizRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp {
    private final CategoryQuizRepository categoryQuizRepository;

    public CategoryServiceImp(CategoryQuizRepository categoryQuizRepository) {
        this.categoryQuizRepository = categoryQuizRepository;
    }

    public CategoryQuiz createCategory(CategoryQuiz categoryQuiz) {
        return categoryQuizRepository.save(categoryQuiz);
    }

    public List<CategoryQuiz> findAllCategories() {
        List<CategoryQuiz> categories = categoryQuizRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("CategoryQuiz list is empty");
        }
        return categories;
    }

    public CategoryQuiz findCategoryById(Long categoryId) {

        return categoryQuizRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("CategoryQuiz not found"));
    }
}
