package com.example.quiz_World.entities;

import lombok.Data;

@Data
public class QuizResultDTO {
    private String user;
    private String quiz;
    private Double score;
}
