package com.example.quiz_world.entities.quizEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerToQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Answer number must be provided")
    private Long answerNumber;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotNull(message = "Correct flag must be provided")
    private boolean correct;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}