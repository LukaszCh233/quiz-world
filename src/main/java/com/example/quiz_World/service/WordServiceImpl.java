package com.example.quiz_World.service;

import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.Status;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.entities.wordSetEntity.*;
import com.example.quiz_World.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WordServiceImpl {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final WordSetRepository wordSetRepository;
    private final MapEntity mapEntity;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final ResultRepository resultRepository;

    public WordServiceImpl(WordRepository wordRepository, UserRepository userRepository, WordSetRepository wordSetRepository,
                           MapEntity mapEntity, WordSetCategoryRepository wordSetCategoryRepository,
                           ResultRepository resultRepository) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
        this.wordSetRepository = wordSetRepository;
        this.mapEntity = mapEntity;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.resultRepository = resultRepository;
    }

    public WordSetDTO createWordSet(String title, Long categoryId, Status status, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        WordSetCategory category = wordSetCategoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        WordSet wordSet = new WordSet();
        wordSet.setTitle(title);
        wordSet.setUserId(user.getId());
        wordSet.setWordSetCategory(category);
        wordSet.setStatus(status);

        wordSetRepository.save(wordSet);
        return mapEntity.mapWordSetToWordSetDTO(wordSet);
    }

    public void addWordToWordSet(Long wordSetId, Word word) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Not found word set"));

        word.setWordSet(wordSet);
        word.setWordNumber(word.getWordNumber());
        word.setTranslation(word.getTranslation());
        wordSet.getWords().add(word);


        wordSetRepository.save(wordSet);
    }

    public List<WordSetDTO> findPublicWordSet() {
        List<WordSet> wordSetList = wordSetRepository.findByStatus(Status.PUBLIC);
        if (wordSetList.isEmpty()) {
            throw new EntityNotFoundException("List is empty");
        }
        return mapEntity.mapWordSetListToWordSetListDTO(wordSetList);
    }

    public List<WordSetDTO> findYourWordSets(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<WordSet> wordSetList = wordSetRepository.findByUserId(user.getId());
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

    public List<WordDTO> findWordsByWordSetId(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (wordSet.getStatus().equals(Status.PRIVATE)) {
            throw new EntityNotFoundException("Word set not found");
        }
        List<Word> wordList = wordSet.getWords();
        if (wordList.isEmpty()) {
            throw new EntityNotFoundException("Word set is empty");
        }
        return mapEntity.mapWordListToWordListDTO(wordList);
    }

    @Transactional
    public void deleteAllWordSetForUser(Principal principal) {
        List<WordSet> wordSets = findWordSetsByUserPrincipal(principal);
        if (wordSets.isEmpty()) {
            throw new EntityNotFoundException("Not Found word sets");
        }
        for (WordSet wordSet : wordSets) {
            resultRepository.deleteByWordSet(wordSet);
        }
        wordSetRepository.deleteAll(wordSets);
    }

    @Transactional
    public void deleteWordSetById(Long wordSetId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Not found word set"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to delete this word set");
        }
        resultRepository.deleteByWordSet(wordSet);

        wordSetRepository.delete(wordSet);
    }

    public void deleteWordByNumberWordSetForUser(Long wordSetId, Long numberWord, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized");
        }
        Word word = wordRepository.findByWordSetIdAndWordNumber(wordSetId, numberWord).orElseThrow(() -> new EntityNotFoundException("Word not found"));

        wordRepository.delete(word);
    }

    public void updateWordSetByIdForUser(Long id, WordSet wordSet, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        if (!wordSetToUpdate.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized");
        }
        if (wordSet.getWordSetCategory() == null) {
            throw new IllegalArgumentException("QuizCategory cannot be null");
        }
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSet.getWordSetCategory().getId()).orElseThrow(() -> new EntityNotFoundException("categoryQuiz not exists"));

        wordSetToUpdate.setTitle(wordSet.getTitle());
        wordSetToUpdate.setWordSetCategory(wordSetCategory);
        wordSetToUpdate.setStatus(wordSet.getStatus());

        wordSetRepository.save(wordSetToUpdate);
    }

    public void updateWordForUser(Long wordSetId, Long wordNumber, Word word, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to update word");
        }
        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSetId, wordNumber).orElseThrow(() -> new EntityNotFoundException("Not found word"));

        wordToUpdate.setWord(word.getWord());
        wordToUpdate.setTranslation(word.getTranslation());
        wordRepository.save(wordToUpdate);

    }

    public double solveWordSet(Long wordSetId, List<AnswerToWordSet> userAnswers, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        Result prviousResult = resultRepository.findByUserIdAndWordSetId(user.getId(), wordSetId);
        int point = 0;
        Collections.shuffle(wordSet.getWords());
        for (AnswerToWordSet userAnswer : userAnswers) {
            for (Word word : wordSet.getWords()) {
                if (word.getTranslation().equalsIgnoreCase(userAnswer.getAnswer())) {
                    point++;
                }
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

    public List<WordSet> findWordSetsByUserPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(wordSetRepository.findByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found word Sets"));

    }

    public void updateWordForAdmin(Long wordSetId, Long wordNumber, Word word) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), wordNumber).orElseThrow(() -> new EntityNotFoundException("Not found word"));

        wordToUpdate.setWord(word.getWord());
        wordToUpdate.setTranslation(word.getTranslation());
        wordRepository.save(wordToUpdate);

    }

    public void updateWordSetByIdForAdmin(Long id, WordSet wordSet) {
        WordSet wordSetToUpdate = wordSetRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        if (wordSet.getWordSetCategory() == null) {
            throw new IllegalArgumentException("Word set category cannot be null");
        }
        WordSetCategory wordSetCategory = wordSetCategoryRepository.findById(wordSet.getWordSetCategory().getId()).orElseThrow(() -> new EntityNotFoundException("categoryQuiz not exists"));

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

    public void deleteWordByNumberWordSetForAdmin(Long wordSetId, Long numberWord) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        Word word = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), numberWord).orElseThrow(() -> new EntityNotFoundException("Word not found"));

        wordRepository.delete(word);
    }

}
