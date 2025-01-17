package com.example.quiz_world.words.wordSet;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerToWordSet {
    @NotBlank(message = "Answer cannot be blank")
    private String answer;
}
