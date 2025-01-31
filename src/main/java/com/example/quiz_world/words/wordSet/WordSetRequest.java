package com.example.quiz_world.words.wordSet;

import com.example.quiz_world.account.user.Status;
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
public class WordSetRequest {
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotNull(message = "Word set category ID must be provided")
    private Long wordSetCategoryId;
    @NotNull(message = "Status must be provided")
    private Status status;
}
