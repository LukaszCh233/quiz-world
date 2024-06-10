package com.example.quiz_World.service.quiz;

import com.example.quiz_World.dto.QuizResultDTO;
import com.example.quiz_World.entities.Result;
import com.example.quiz_World.entities.User;
import com.example.quiz_World.mapper.MapEntity;
import com.example.quiz_World.repository.ResultRepository;
import com.example.quiz_World.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class QuizResultService {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final MapEntity mapEntity;

    public QuizResultService(ResultRepository resultRepository, UserRepository userRepository, MapEntity mapEntity) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.mapEntity = mapEntity;
    }

    public List<QuizResultDTO> findQuizzesResults() {
        List<Result> results = resultRepository.findAll();
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapEntity.mapQuizResultsToQuizResultsDTO(results);
    }

    public List<QuizResultDTO> findYourQuizzesResults(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapEntity.mapQuizResultsToQuizResultsDTO(results);
    }
}
