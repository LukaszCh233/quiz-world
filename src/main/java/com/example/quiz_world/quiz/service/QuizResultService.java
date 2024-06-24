package com.example.quiz_world.quiz.service;

import com.example.quiz_world.mapper.MapperEntity;
import com.example.quiz_world.quiz.dto.QuizResultDTO;
import com.example.quiz_world.reslult.Result;
import com.example.quiz_world.reslult.ResultRepository;
import com.example.quiz_world.user.entity.User;
import com.example.quiz_world.user.repository.UserRepository;
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        List<Result> results = resultRepository.findByUserId(user.getId());
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Score list is empty");
        }
        return mapperEntity.mapQuizResultsToQuizResultsDTO(results);
    }
}
