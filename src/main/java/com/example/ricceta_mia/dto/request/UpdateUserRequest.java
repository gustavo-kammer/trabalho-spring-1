package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.RecipeCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UpdateUserRequest(
        @Schema(description = "Nome do usuário", example = "Maria")
        @NotEmpty(message = "Campo nome é obrigatório") @Size(max = 150) String firstName,
        @Schema(description = "Sobrenome", example = "Silva") @Size(max = 150) String lastName,
        @Schema(description = "Apelido único", example = "maria_cozinha") @Size(max = 100) String nickname,
        @Schema(description = "Cidade ou região", example = "São Paulo, SP") @Size(max = 150) String cityRegion,
        @Schema(description = "Apresentação do perfil", example = "Apaixonada por culinária italiana.") String bio,
        @Schema(description = "Categorias preferidas. Omitir mantém as atuais; enviar [] limpa a seleção.", example = "[\"ITALIAN\"]")
        Set<RecipeCategory> preferredCategories) {
}
