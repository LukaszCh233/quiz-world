package com.example.quiz_world.words.wordSetCategory;

import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordSetCategoryService {
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final MapperEntity mapperEntity;

    public WordSetCategoryService(WordSetCategoryRepository wordSetCategoryRepository, MapperEntity mapperEntity) {
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.mapperEntity = mapperEntity;
    }

    public WordSetCategory createWordSetCategory(WordSetCategory wordSetCategory) {
        Optional<WordSetCategory> existingCategory = wordSetCategoryRepository.findByNameIgnoreCase(wordSetCategory.getName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Category exists");
        }
        return wordSetCategoryRepository.save(wordSetCategory);
    }

    public List<WordSetCategoryDTO> findAllWordSetCategories() {
        List<WordSetCategory> categories = wordSetCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Word set list is empty");
        }
        return mapperEntity.mapWordSetCategoriesToWordSetCategoriesDTO(categories);
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
}
