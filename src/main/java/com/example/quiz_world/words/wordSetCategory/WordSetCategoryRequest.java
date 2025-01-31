package com.example.quiz_world.words.wordSetCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WordSetCategoryRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;
}
