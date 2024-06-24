package com.example.quiz_world.words.entity;

import com.example.quiz_world.user.entity.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Title must not be blank")
    private String title;
    @OneToMany(mappedBy = "wordSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Word> words;
    @NotNull(message = "User ID must be provided")
    private Long userId;
    @ManyToOne
    private WordSetCategory wordSetCategory;

    @NotNull(message = "Status must be provided")
    private Status status;
}
