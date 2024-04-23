package com.example.quiz_World.entities;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String questionContent;
    private List<AnswerDTO> answers;
    private Long questionNumber;
}
