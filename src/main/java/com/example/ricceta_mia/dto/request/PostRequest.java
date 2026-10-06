package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.RecipeCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostRequest(@NotEmpty(message = "Campo título é obrigatório") @Size(max = 200) String title,
                          @Size(max = 500) String photoUrl,
                          String description,
                          @NotEmpty(message = "Campo ingredientes é obrigatório") String ingredients,
                          @NotEmpty(message = "Campo modo de preparo é obrigatório") String instructions,
                          Set<RecipeCategory> categories) {
}
