package com.example.quiz_World.entities.quizEntity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuestionDTO {
    private Long questionNumber;
    private String questionContent;
    private List<AnswerToQuizDTO> answers;

}
