package com.example.quiz_World.entities.quizEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerToQuiz {
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