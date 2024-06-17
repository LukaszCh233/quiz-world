package com.example.quiz_world.entities.quizEntity;

import com.example.quiz_world.entities.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotNull(message = "Quiz category must be provided")
    @ManyToOne
    private QuizCategory quizCategory;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions;
    @NotNull(message = "Status must be provided")
    private Status status;
}
