package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(@NotBlank(message = "Campo conteúdo é obrigatório") String content) {
}
