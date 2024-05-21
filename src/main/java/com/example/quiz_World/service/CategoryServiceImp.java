package com.example.quiz_World.service;

import com.example.quiz_World.entities.quizEntity.QuizCategory;
import com.example.quiz_World.entities.quizEntity.QuizCategoryDTO;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategoryDTO;
import com.example.quiz_World.repository.QuizCategoryRepository;
import com.example.quiz_World.repository.WordSetCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp {
    private final QuizCategoryRepository quizCategoryRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final MapEntity mapEntity;

    public CategoryServiceImp(QuizCategoryRepository quizCategoryRepository, WordSetCategoryRepository wordSetCategoryRepository,
                              MapEntity mapEntity) {
        this.quizCategoryRepository = quizCategoryRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.mapEntity = mapEntity;
    }

    public QuizCategory createQuizCategory(QuizCategory quizCategory) {
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

    public WordSetCategory createWordSetCategory(WordSetCategory categoryWordSet) {
        return wordSetCategoryRepository.save(categoryWordSet);
    }

    public List<WordSetCategoryDTO> findAllWordSetCategories() {
        List<WordSetCategory> categories = wordSetCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Word set list is empty");
        }
        return mapEntity.mapWordSetCategoriesToWordSetCategoriesDTO(categories);
    }

    @Transactional
    public void deleteWordSetCategoryById(Long categoryId) {
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Word set category not found"));

        wordSetCategoryRepository.delete(wordSetCategory);
    }

    @Transactional
    public void deleteAllWordSetCategories() {
        List<WordSetCategory> wordSetCategoryList = wordSetCategoryRepository.findAll();
        if (wordSetCategoryList.isEmpty()) {
            throw new EntityNotFoundException("Not found categories in Word set");
        }
        wordSetCategoryRepository.deleteAll();
    }

    public WordSetCategory updateWordSetCategory(Long idCategory, WordSetCategory updateCategory) {
        WordSetCategory presentCategory = wordSetCategoryRepository.findById(idCategory).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        presentCategory.setName(updateCategory.getName());
        return wordSetCategoryRepository.save(presentCategory);
    }

    public QuizCategory updateQuizCategory(Long idCategory, QuizCategory updateCategory) {
        QuizCategory presentCategory = quizCategoryRepository.findById(idCategory).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        presentCategory.setName(updateCategory.getName());
        return quizCategoryRepository.save(presentCategory);
    }
}
