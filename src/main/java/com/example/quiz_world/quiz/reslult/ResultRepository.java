package com.example.quiz_world.quiz.reslult;

import com.example.quiz_world.quiz.quiz.Quiz;
import com.example.quiz_world.words.wordSet.WordSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    void deleteByQuiz(Quiz quiz);

    void deleteByQuizIn(List<Quiz> quizList);

    void deleteByWordSet(WordSet wordSet);

    List<Result> findByUserId(Long id);

    Result findByUserIdAndWordSetId(Long userId, Long wordSetId);

    Result findByUserIdAndQuizId(Long userId, Long quizId);
}
