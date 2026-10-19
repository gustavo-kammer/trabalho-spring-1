package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record CommentRequest(
        @Schema(description = "Texto do comentário", example = "Fiz esta receita e ficou deliciosa!")
        @NotBlank(message = "Campo conteúdo é obrigatório") String content) {
}
