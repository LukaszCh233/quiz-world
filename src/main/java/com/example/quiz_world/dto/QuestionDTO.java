package com.example.quiz_world.dto;

import java.util.List;

public record QuestionDTO(Long questionNumber, String questionContent, List<AnswerToQuizDTO> answers) {

}
