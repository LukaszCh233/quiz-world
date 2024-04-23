package com.example.quiz_World.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class WordSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Title;
    @OneToMany(mappedBy = "wordSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> words;
    private Long userId;
    @ManyToOne
    private CategoryWordSet categoryWordSet;
    private Status status;
}
