package com.example.quiz_World.dto;

import java.util.List;

public record QuestionDTO(Long questionNumber, String questionContent, List<AnswerToQuizDTO> answers) {

}
