package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "E-mail cadastrado", example = "maria@example.com")
        @NotEmpty(message = "E-mail é obrigatório") String email,
        @Schema(description = "Senha cadastrada", example = "Exemplo123!", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotEmpty(message = "Senha é obrigatória") String password) {



}
