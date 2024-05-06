package com.example.quiz_World.entities;

import com.example.quiz_World.entities.quizEntity.Quiz;
import com.example.quiz_World.entities.wordSetEntity.WordSet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    @ManyToOne
    @JoinColumn(name = "wordSet_id")
    private WordSet wordSet;

    private Double score;
}
