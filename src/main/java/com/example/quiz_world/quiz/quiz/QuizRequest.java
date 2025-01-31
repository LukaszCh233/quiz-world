package com.example.quiz_world.quiz.quiz;

import com.example.quiz_world.account.user.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizRequest {
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotNull(message = "Quiz category ID must be provided")
    private Long quizCategoryId;
    @NotNull(message = "Status must be provided")
    private Status status;
}
