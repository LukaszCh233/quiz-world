package com.example.quiz_world.quiz.quizCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuizCategoryRequest {
    @NotBlank(message = "Name cannot be blank")
    private String categoryName;
}
