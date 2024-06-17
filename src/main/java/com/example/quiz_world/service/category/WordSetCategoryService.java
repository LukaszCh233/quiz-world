package com.example.quiz_world.service.category;

import com.example.quiz_world.dto.WordSetCategoryDTO;
import com.example.quiz_world.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_world.mapper.MapEntity;
import com.example.quiz_world.repository.words.WordSetCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordSetCategoryService {
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final MapEntity mapEntity;

    public WordSetCategoryService(WordSetCategoryRepository wordSetCategoryRepository, MapEntity mapEntity) {
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.mapEntity = mapEntity;
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
}
