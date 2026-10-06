package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.RecipeCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(@NotEmpty(message = "Campo nome é obrigatório") @Size(max = 150) String firstName,
                                @Size(max = 150) String lastName,
                                @Size(max = 100) String nickname,
                                @Size(max = 150) String cityRegion,
                                String bio,
                                Set<RecipeCategory> preferredCategories) {
}
