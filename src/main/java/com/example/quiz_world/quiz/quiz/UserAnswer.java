package com.example.quiz_world.quiz.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {
    @NotNull(message = "Answer must be provided")
    private Long answer;
}
