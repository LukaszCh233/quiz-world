package com.example.quiz_world.quiz.reslult;

import com.example.quiz_world.account.user.User;
import com.example.quiz_world.account.user.UserRepository;
import com.example.quiz_world.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class QuizResultService {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final MapperEntity mapperEntity;

    public QuizResultService(ResultRepository resultRepository, UserRepository userRepository, MapperEntity mapperEntity) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.mapperEntity = mapperEntity;
    }

    public List<QuizResultDTO> findQuizzesResults() {
        List<Result> results = resultRepository.findAll();
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapperEntity.mapQuizResultsToQuizResultsDTO(results);
    }

    public List<QuizResultDTO> findYourQuizzesResults(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapperEntity.mapQuizResultsToQuizResultsDTO(results);
    }
}
