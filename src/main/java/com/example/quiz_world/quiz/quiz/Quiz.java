package com.example.quiz_world.quiz.quiz;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.quiz.question.Question;
import com.example.quiz_world.quiz.quizCategory.QuizCategory;
import jakarta.persistence.*;
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
    private String title;
    @ManyToOne
    private QuizCategory quizCategory;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions;
    private Status status;
}
