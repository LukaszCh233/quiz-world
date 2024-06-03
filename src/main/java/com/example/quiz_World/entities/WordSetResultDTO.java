package com.example.quiz_World.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WordSetResultDTO {
    private String user;
    private String wordSet;
    private Double score;
}
