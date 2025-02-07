package com.example.quiz_world.words.word;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.reslult.Result;
import com.example.quiz_world.quiz.reslult.ResultRepository;
import com.example.quiz_world.words.wordSet.WordSet;
import com.example.quiz_world.words.wordSet.WordSetRepository;
import com.example.quiz_world.words.wordSet.WordSetResultDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class WordsService {
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final WordSetRepository wordSetRepository;
    private final MapperEntity mapperEntity;
    private final ResultRepository resultRepository;

    public WordsService(WordRepository wordRepository, UserRepository userRepository,
                        WordSetRepository wordSetRepository, MapperEntity mapperEntity,
                        ResultRepository resultRepository) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
        this.wordSetRepository = wordSetRepository;
        this.mapperEntity = mapperEntity;
        this.resultRepository = resultRepository;
    }

    public void addWordToWordSet(Long wordSetId, WordRequest wordRequest) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        boolean wordNumberExists = wordSet.getWords().stream()
                .anyMatch(w -> w.getWordNumber().equals(wordRequest.getWordNumber()));
        if (wordNumberExists) {
            throw new IllegalArgumentException("number " + wordRequest.getWordNumber() +
                    " already exists in this word set");
        }
        Word word = new Word();
        word.setWordSet(wordSet);
        word.setWordNumber(wordRequest.getWordNumber());
        word.setWord(wordRequest.getWord());
        word.setTranslation(wordRequest.getTranslation());
        wordSet.getWords().add(word);

        wordSetRepository.save(wordSet);
    }

    public List<WordDTO> findWordsByWordSetId(Long wordSetId) {
        WordSet wordSet = wordSetRepository.findByIdAndStatus(wordSetId, Status.PUBLIC).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        List<Word> wordList = wordSet.getWords();
        if (wordList.isEmpty()) {
            throw new EntityNotFoundException("Word set is empty");
        }
        return mapperEntity.mapWordListToWordListDTO(wordList);
    }

    public void deleteWordByNumberAndWordSetForUser(Long wordSetId, Long numberWord, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized");
        }
        Word word = wordRepository.findByWordSetIdAndWordNumber(wordSetId, numberWord).orElseThrow(() ->
                new EntityNotFoundException("Word not found"));

        wordRepository.delete(word);
    }

    public void updateWordForUser(Long wordSetId, Long wordNumber, WordRequest wordRequest, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Not found user"));

        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));
        if (!wordSet.getUserId().equals(user.getId())) {
            throw new UnsupportedOperationException("User is not authorized to update word");
        }
        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSetId, wordNumber).orElseThrow(() ->
                new EntityNotFoundException("Not found word"));

        wordToUpdate.setWord(wordRequest.getWord());
        wordToUpdate.setTranslation(wordRequest.getTranslation());

        wordRepository.save(wordToUpdate);
    }

    public List<WordSetResultDTO> findYourWordsResults(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapperEntity.mapWordSetResultsToWordSetResultsDTO(results);
    }

    public void updateWordForAdmin(Long wordSetId, Long wordNumber, WordRequest wordRequest) {
        WordSet wordSet = wordSetRepository.findById(wordSetId).orElseThrow(() ->
                new EntityNotFoundException("Word set not found"));

        Word wordToUpdate = wordRepository.findByWordSetIdAndWordNumber(wordSet.getId(), wordNumber).orElseThrow(() ->
                new EntityNotFoundException("Word not found"));

        wordToUpdate.setWord(wordRequest.getWord());
        wordToUpdate.setTranslation(wordRequest.getTranslation());

        wordRepository.save(wordToUpdate);
    }

    public void deleteWordByNumberAndWordSetForAdmin(Long wordSetId, Long numberWord) {
        Word word = wordRepository.findByWordSetIdAndWordNumber(wordSetId, numberWord).orElseThrow(() ->
                new EntityNotFoundException("Word not found"));

        wordRepository.delete(word);
    }
}
