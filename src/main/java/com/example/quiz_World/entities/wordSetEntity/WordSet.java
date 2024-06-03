package com.example.quiz_World.entities.wordSetEntity;

import com.example.quiz_World.entities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WordSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToMany(mappedBy = "wordSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Word> words;
    private Long userId;
    @ManyToOne
    private WordSetCategory wordSetCategory;
    private Status status;
}
