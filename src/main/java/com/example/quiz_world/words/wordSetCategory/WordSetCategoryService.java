package com.example.quiz_world.words.wordSetCategory;

import com.example.quiz_world.exception.ExistsException;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.words.wordSet.WordSetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordSetCategoryService {
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final WordSetRepository wordSetRepository;
    private final MapperEntity mapperEntity;

    public WordSetCategoryService(WordSetCategoryRepository wordSetCategoryRepository,
                                  WordSetRepository wordSetRepository, MapperEntity mapperEntity) {
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.wordSetRepository = wordSetRepository;
        this.mapperEntity = mapperEntity;
    }

    public WordSetCategoryDTO createWordSetCategory(WordSetCategoryRequest wordSetCategoryRequest) {
        Optional<WordSetCategory> existingCategory = wordSetCategoryRepository
                .findByNameIgnoreCase(wordSetCategoryRequest.getName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Category exists");
        }
        WordSetCategory wordSetCategory = new WordSetCategory();
        wordSetCategory.setName(wordSetCategoryRequest.getName());

        wordSetCategoryRepository.save(wordSetCategory);

        return mapperEntity.mapWordSetCategoryToWordSSetCategoryDTO(wordSetCategory);
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
        WordSetCategory category = wordSetCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        if (wordSetRepository.existsByWordSetCategoryId(categoryId)) {
            throw new IllegalStateException("Cannot delete category with associated WordSets.");
        }
        wordSetCategoryRepository.delete(category);
    }

    @Transactional
    public void deleteAllWordSetCategories() {
        if (wordSetRepository.findFirstBy().isPresent()) {
            throw new IllegalStateException("Cannot delete categories because some WordSets are still associated with them.");
        }
        wordSetCategoryRepository.deleteAll();
    }

    public WordSetCategoryDTO updateWordSetCategory(Long idCategory, WordSetCategoryRequest wordSetCategoryRequest) {
        WordSetCategory categoryToUpdate = wordSetCategoryRepository.findById(idCategory).orElseThrow(() ->
                new EntityNotFoundException("Category not found"));
        if (wordSetCategoryRepository.existsByName(wordSetCategoryRequest.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        categoryToUpdate.setName(wordSetCategoryRequest.getName());

        wordSetCategoryRepository.save(categoryToUpdate);

        return mapperEntity.mapWordSetCategoryToWordSSetCategoryDTO(categoryToUpdate);
    }
}
