package com.example.quiz_world.quiz.reslult;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByUserId(Long id);

    Result findByUserIdAndWordSetId(Long userId, Long wordSetId);

    Result findByUserIdAndQuizId(Long userId, Long quizId);
}
