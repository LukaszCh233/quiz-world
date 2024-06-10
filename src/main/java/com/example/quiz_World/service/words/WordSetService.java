package com.example.quiz_World.service.words;

import com.example.quiz_World.dto.WordSetDTO;
import com.example.quiz_World.entities.Admin;
import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.wordSetEntity.AnswerToWordSet;
import com.example.quiz_World.entities.wordSetEntity.Word;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import com.example.quiz_World.entities.wordSetEntity.WordSetCategory;
import com.example.quiz_World.mapper.MapEntity;
import com.example.quiz_World.repository.AdminRepository;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import com.example.quiz_World.repository.words.WordSetCategoryRepository;
import com.example.quiz_World.repository.words.WordSetRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WordSetService {
    private final UserRepository userRepository;
    private final WordSetRepository wordSetRepository;
    private final ResultRepository resultRepository;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final AdminRepository adminRepository;
    private final MapEntity mapEntity;


    public WordSetService(UserRepository userRepository, WordSetRepository wordSetRepository,
                          ResultRepository resultRepository, WordSetCategoryRepository wordSetCategoryRepository,
                          AdminRepository adminRepository, MapEntity mapEntity) {
        this.userRepository = userRepository;
        this.wordSetRepository = wordSetRepository;
        this.resultRepository = resultRepository;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.adminRepository = adminRepository;
        this.mapEntity = mapEntity;

    }

    public double solveWordSet(Long wordSetId, List<AnswerToWordSet> userAnswers, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        Result prviousResult = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSetId);

        List<Word> words = new ArrayList<>(wordSet.getWords());

        Collections.shuffle(words);

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
        Optional<User> user = userRepository.findByEmail(email);

        WordSetCategory category = wordSetCategoryRepository.findById(wordSetCategoryId).orElseThrow(() -> new EntityNotFoundException("category not found"));

        WordSet wordSet = new WordSet();

        if (user.isPresent()) {
            wordSet.setTitle(title);
            wordSet.setWordSetCategory(category);
            wordSet.setUserId(user.get().getId());
            wordSet.setStatus(status);
        } else {
            Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

            wordSet.setTitle(title);
            wordSet.setWordSetCategory(category);
            wordSet.setUserId(admin.getId());
            wordSet.setStatus(status);
        }
        wordSetRepository.save(wordSet);

        return mapEntity.mapWordSetToWordSetDTO(wordSet);
    }

    public List<WordSetDTO> findPublicWordSets() {
        List<WordSet> wordSetList = wordSetRepository.findByStatus(Status.PUBLIC);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findYourWordSets(Principal principal) {
        String email = principal.getName();

        List<WordSet> wordSetList;

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            wordSetList = wordSetRepository.findByUserId(user.get().getId());
        } else {
            Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

            wordSetList = wordSetRepository.findByUserId(admin.getId());
        }
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Not found word sets");
        }
        return mapEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findWordSetByCategory(Long categoryId) {
        List<WordSet> wordSetList = wordSetRepository.findByWordSetCategoryId(categoryId);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("Not found word set with this category");
        }
        return mapEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public WordSetDTO findWordSetById(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (wordSet.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Word set not found");
        }
        return mapEntity.mapWordSetToWordSetDTO(wordSet);
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

    public WordSet updateWordSetByIdForUser(Long id, WordSet wordSet, Principal principal) {
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

        return wordSetRepository.save(wordSetToUpdate);
    }

    public WordSet updateWordSetByIdForAdmin(Long id, WordSet wordSet) {
        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        if (wordSet.getWordSetCategory() == null) {
            throw new IllegalArgumentException("Word set category cannot be null");
        }
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSet.getWordSetCategory().getId()).orElseThrow(() -> new EntityNotFoundException("category quiz not exists"));

        wordSetToUpdate.setTitle(wordSet.getTitle());
        wordSetToUpdate.setWordSetCategory(wordSetCategory);
        wordSetToUpdate.setStatus(wordSet.getStatus());

        return wordSetRepository.save(wordSetToUpdate);
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
