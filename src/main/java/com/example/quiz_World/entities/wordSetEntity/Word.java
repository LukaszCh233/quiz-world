package com.example.quiz_World.entities.wordSetEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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