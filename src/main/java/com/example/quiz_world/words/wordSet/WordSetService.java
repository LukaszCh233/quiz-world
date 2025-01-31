package com.example.quiz_world.words.wordSet;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.quiz.reslult.ResultRepository;
import com.example.quiz_world.words.word.Word;
import com.example.quiz_world.words.wordSetCategory.WordSetCategory;
import com.example.quiz_world.words.wordSetCategory.WordSetCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));
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

    public WordSetDTO createWordSet(WordSetRequest wordSetRequest, Principal principal) {
        String email = principal.getName();

        WordSetCategory category = wordSetCategoryRepository.findById(wordSetRequest.getWordSetCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        WordSet wordSet = new WordSet();
        wordSet.setTitle(wordSetRequest.getTitle());
        wordSet.setWordSetCategory(category);
        wordSet.setUserId(user.getId());
        wordSet.setStatus(wordSetRequest.getStatus());

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

    public List<WordSetDTO> findWordSetsByUserPrincipal(Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<WordSet> wordSetList = wordSetRepository.findByUserId(user.getId());
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Word sets not found");
        }
        return mapperEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findWordSetByCategoryId(Long categoryId) {
        List<WordSet> wordSetList = wordSetRepository.findByWordSetCategoryIdAndStatus(categoryId, Status.PUBLIC);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Not found word set with this category");
        }
        return mapperEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public WordSetDTO findWordSetById(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        if (wordSet.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Word set not found");
        }
        return mapperEntity.mapWordSetToWordSetDTO(wordSet);
    }

    @Transactional
    public void deleteAllWordSetsForUser(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        wordSetRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public void deleteWordSetByIdForUser(Long wordSetId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Not found word set"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this word set");
        }
        wordSetRepository.delete(wordSet);
    }

    public void updateWordSetByIdForUser(Long id, WordSetRequest wordSetRequest, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        if (!wordSetToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized");
        }
        updateWordSet(wordSetToUpdate, wordSetRequest);

        wordSetRepository.save(wordSetToUpdate);
    }

    public void updateWordSetByIdForAdmin(Long id, WordSetRequest wordSetRequest) {
        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        updateWordSet(wordSetToUpdate, wordSetRequest);

        wordSetRepository.save(wordSetToUpdate);
    }

    private void updateWordSet(WordSet wordSetToUpdate, WordSetRequest wordSetRequest) {
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSetRequest.getWordSetCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Word set category not exists"));

        wordSetToUpdate.setTitle(wordSetRequest.getTitle());
        wordSetToUpdate.setWordSetCategory(wordSetCategory);
        wordSetToUpdate.setStatus(wordSetRequest.getStatus());
    }

    @Transactional
    public void deleteAllWordSetsForAdmin() {
        wordSetRepository.deleteAll();
    }

    @Transactional
    public void deleteWordSetByIdForAdmin(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Not found word set"));

        wordSetRepository.delete(wordSet);
    }
}
