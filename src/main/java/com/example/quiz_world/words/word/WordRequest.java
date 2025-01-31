package com.example.quiz_world.words.word;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WordRequest {
    @NotNull(message = "Word number must be provided")
    private Long wordNumber;
    @NotBlank(message = "Word must not be blank")
    private String word;
    @NotBlank(message = "Translation must not be blank")
    private String translation;
}
