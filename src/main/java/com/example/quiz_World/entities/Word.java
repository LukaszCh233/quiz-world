package com.example.quiz_World.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long wordNumber;
    private String word;
    private String translation;
    @ManyToOne
    @JoinColumn(name = "wordSet_id")
    private WordSet wordSet;
}
