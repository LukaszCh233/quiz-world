package com.example.quiz_World.entities.quizEntity;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long questionNumber;
    private String questionContent;
    private List<AnswerToQuizDTO> answers;

}
