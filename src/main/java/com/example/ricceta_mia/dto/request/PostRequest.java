package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.RecipeCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record PostRequest(
        @Schema(description = "Título da receita", example = "Macarrão ao molho de tomate")
        @NotEmpty(message = "Campo título é obrigatório") @Size(max = 200) String title,
        @Schema(description = "Endereço opcional da foto", example = "https://example.com/macarrao.jpg")
        @Size(max = 500) String photoUrl,
        @Schema(description = "Apresentação da receita", example = "Receita simples para o almoço.") String description,
        @Schema(description = "Ingredientes e quantidades", example = "200 g de macarrão; 2 tomates; sal a gosto")
        @NotEmpty(message = "Campo ingredientes é obrigatório") String ingredients,
        @Schema(description = "Modo de preparo", example = "Cozinhe o macarrão, prepare o molho e misture.")
        @NotEmpty(message = "Campo modo de preparo é obrigatório") String instructions,
        @Schema(description = "Categorias; quando omitidas, a receita fica sem categorias", example = "[\"ITALIAN\", \"VEGETARIAN\"]")
        Set<RecipeCategory> categories) {
}
