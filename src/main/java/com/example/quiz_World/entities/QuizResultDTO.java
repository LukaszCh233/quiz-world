package com.example.quiz_World.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizResultDTO {
    private String user;
    private String quiz;
    private Double score;

}
