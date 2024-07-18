package com.example.quiz_world.words.service;

import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.reslult.Result;
import com.example.quiz_world.reslult.ResultRepository;
import com.example.quiz_world.user.entity.Status;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
import com.example.quiz_world.words.dto.WordSetDTO;
import com.example.quiz_world.words.entity.AnswerToWordSet;
import com.example.quiz_world.words.entity.Word;
import com.example.quiz_world.words.entity.WordSet;
import com.example.quiz_world.words.entity.WordSetCategory;
import com.example.quiz_world.words.repository.WordSetCategoryRepository;
import com.example.quiz_world.words.repository.WordSetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WordSetService {
    private final UserRepository userRepository;
    private final WordSetRepository wordSetRepository;
    private final ResultRepository resultRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final MapperEntity mapperEntity;


    public WordSetService(UserRepository userRepository, WordSetRepository wordSetRepository,
                          ResultRepository resultRepository, WordSetCategoryRepository wordSetCategoryRepository,
                          MapperEntity mapperEntity) {
        this.userRepository = userRepository;
        this.wordSetRepository = wordSetRepository;
        this.resultRepository = resultRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.mapperEntity = mapperEntity;
    }

    public double solveWordSet(Long wordSetId, List<AnswerToWordSet> userAnswers, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        Result prviousResult = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSetId);

        List<Word> words = new ArrayList<>(wordSet.getWords());

        int point = 0;

        if (userAnswers.size() != words.size()) {
            throw new IllegalArgumentException("Number of user answers does not match the number of words in the set");
        }
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            AnswerToWordSet userAnswer = userAnswers.get(i);

            if (word.getTranslation().equalsIgnoreCase(userAnswer.getAnswer())) {
                point++;
            }
        }
        double score = (double) point / wordSet.getWords().size() * 100;
        if (prviousResult == null || score > prviousResult.getScore()) {

            Result result = new Result();
            result.setUser(user);
            result.setScore(score);
            result.setWordSet(wordSet);
            resultRepository.save(result);
        }
        if (prviousResult != null && score > prviousResult.getScore()) {
            resultRepository.delete(prviousResult);
        }
        return score;
    }

    public WordSetDTO createWordSet(String title, Long wordSetCategoryId, Status status, Principal principal) {
        String email = principal.getName();

        WordSetCategory category = wordSetCategoryRepository.findById(wordSetCategoryId).orElseThrow(() -> new EntityNotFoundException("category not found"));

        WordSet wordSet = new WordSet();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        wordSet.setTitle(title);
        wordSet.setWordSetCategory(category);
        wordSet.setUserId(user.getId());
        wordSet.setStatus(status);

        wordSetRepository.save(wordSet);

        return mapperEntity.mapWordSetToWordSetDTO(wordSet);
    }

    public List<WordSetDTO> findPublicWordSets() {
        List<WordSet> wordSetList = wordSetRepository.findByStatus(Status.PUBLIC);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapperEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findYourWordSets(Principal principal) {
        String email = principal.getName();

        List<WordSet> wordSetList;

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        wordSetList = wordSetRepository.findByUserId(user.getId());
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Not found word sets");
        }
        return mapperEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findWordSetByCategory(Long categoryId) {
        List<WordSet> wordSetList = wordSetRepository.findByWordSetCategoryId(categoryId);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Not found word set with this category");
        }
        List<WordSet> publicQuizzes = wordSetList.stream()
                .filter(wordSet -> wordSet.getStatus().equals(Status.PUBLIC))
                .collect(Collectors.toList());

        return mapperEntity.mapWordSetListToWordSetListDTO(publicQuizzes);
    }

    public WordSetDTO findWordSetById(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (wordSet.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Word set not found");
        }
        return mapperEntity.mapWordSetToWordSetDTO(wordSet);
    }

    public List<WordSet> findWordSetsByUserPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(wordSetRepository.findByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found word sets"));
    }

    @Transactional
    public void deleteAllWordSetsForUser(Principal principal) {
        List<WordSet> wordSets = findWordSetsByUserPrincipal(principal);
        if (wordSets.isEmpty()) {
            throw new EntityNotFoundException("Not found word sets");
        }
        for (WordSet wordSet : wordSets) {
            resultRepository.deleteByWordSet(wordSet);
        }
        wordSetRepository.deleteAll(wordSets);
    }

    @Transactional
    public void deleteWordSetByIdForUser(Long wordSetId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Not found word set"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this word set");
        }
        resultRepository.deleteByWordSet(wordSet);

        wordSetRepository.delete(wordSet);
    }

    public void updateWordSetByIdForUser(Long id, WordSet wordSet, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (!wordSetToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized");
        }
        if (wordSet.getWordSetCategory() == null) {
            throw new IllegalArgumentException("Word set category cannot be null");
        }
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSet.getWordSetCategory().getId()).orElseThrow(() -> new EntityNotFoundException("categoryQuiz not exists"));

        wordSetToUpdate.setTitle(wordSet.getTitle());
        wordSetToUpdate.setWordSetCategory(wordSetCategory);
        wordSetToUpdate.setStatus(wordSet.getStatus());

        wordSetRepository.save(wordSetToUpdate);
    }

    public void updateWordSetByIdForAdmin(Long id, WordSet wordSet) {
        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        if (wordSet.getWordSetCategory() == null) {
            throw new IllegalArgumentException("Word set category cannot be null");
        }
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSet.getWordSetCategory().getId()).orElseThrow(() -> new EntityNotFoundException("category quiz not exists"));

        wordSetToUpdate.setTitle(wordSet.getTitle());
        wordSetToUpdate.setWordSetCategory(wordSetCategory);
        wordSetToUpdate.setStatus(wordSet.getStatus());

        wordSetRepository.save(wordSetToUpdate);
    }

    @Transactional
    public void deleteAllWordSetsForAdmin() {
        List<WordSet> wordSets = wordSetRepository.findAll();
        for (WordSet wordSet : wordSets) {
            resultRepository.deleteByWordSet(wordSet);
        }
        wordSetRepository.deleteAll(wordSets);
    }

    @Transactional
    public void deleteWordSetByIdForAdmin(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Not found word set"));

        resultRepository.deleteByWordSet(wordSet);

        wordSetRepository.delete(wordSet);
    }
}
