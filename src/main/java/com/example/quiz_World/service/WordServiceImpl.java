package com.example.quiz_World.service;

import com.example.quiz_World.entities.*;
import com.example.quiz_World.entities.wordSetEntity.*;
import com.example.quiz_World.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WordServiceImpl {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final WordSetRepository wordSetRepository;
    private final MapEntity mapEntity;
    private final WordSetCategoryRepository wordSetCategoryRepository;
    private final ResultRepository resultRepository;

    public WordServiceImpl(WordRepository wordRepository, UserRepository userRepository, AdminRepository adminRepository, WordSetRepository wordSetRepository,
                           MapEntity mapEntity, WordSetCategoryRepository wordSetCategoryRepository,
                           ResultRepository resultRepository) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.wordSetRepository = wordSetRepository;
        this.mapEntity = mapEntity;
        this.wordSetCategoryRepository = wordSetCategoryRepository;
        this.resultRepository = resultRepository;
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

    public void addWordToWordSet(Long wordSetId, Word word) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Not found word set"));

        word.setWordSet(wordSet);
        word.setWordNumber(word.getWordNumber());
        word.setWord(word.getWord());
        word.setTranslation(word.getTranslation());
        wordSet.getWords().add(word);

        wordSetRepository.save(wordSet);
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

    public Word updateWordForUser(Long wordSetId, Long wordNumber, Word word, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to update word");
        }
        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSetId, wordNumber).orElseThrow(() -> new EntityNotFoundException("Not found word"));

        wordToUpdate.setWord(word.getWord());
        wordToUpdate.setTranslation(word.getTranslation());

        return wordRepository.save(wordToUpdate);
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
    public List<WordSetResultDTO> findYourWordsResults(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapEntity.mapWordSetResultsToWordSetResultsDTO(results);
    }

    public List<WordSet> findWordSetsByUserPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        return Optional.ofNullable(wordSetRepository.findByUserId(user.getId())).orElseThrow(() -> new EntityNotFoundException("Not found word sets"));
    }

    public Word updateWordForAdmin(Long wordSetId, Long wordNumber, Word word) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), wordNumber).orElseThrow(() -> new EntityNotFoundException("Not found word"));

        wordToUpdate.setWord(word.getWord());
        wordToUpdate.setTranslation(word.getTranslation());

        return wordRepository.save(wordToUpdate);
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

    public void deleteWordByNumberWordSetForAdmin(Long wordSetId, Long numberWord) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() -> new EntityNotFoundException("Word set not found"));

        Word word = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), numberWord).orElseThrow(() -> new EntityNotFoundException("Word not found"));

        wordRepository.delete(word);
    }
}
