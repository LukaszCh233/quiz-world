package com.example.quiz_world.quiz.reslult;

import com.example.quiz_world.account.user.User;
import com.example.quiz_world.quiz.quiz.Quiz;
import com.example.quiz_world.words.wordSet.WordSet;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    @ManyToOne
    @JoinColumn(name = "wordSet_id")
    private WordSet wordSet;
    private Double score;
}
