package com.example.quiz_World.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AnswerToQuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long answerNumber;
    private String content;
    private boolean correct;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}