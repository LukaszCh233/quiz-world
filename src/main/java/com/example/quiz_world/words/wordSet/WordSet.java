package com.example.quiz_world.words.wordSet;

import com.example.quiz_world.account.user.Status;
import com.example.quiz_world.words.word.Word;
import com.example.quiz_world.words.wordSetCategory.WordSetCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
