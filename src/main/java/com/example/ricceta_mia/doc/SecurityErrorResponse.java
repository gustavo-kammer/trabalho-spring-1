package com.example.ricceta_mia.doc;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * Modelo de documentação da resposta padrão do Spring Boot para bloqueios de segurança.
 */
@Schema(description = "Resposta de acesso bloqueado antes de executar o controller")
public record SecurityErrorResponse(
        Instant timestamp,
        @Schema(example = "403") int status,
        @Schema(example = "Forbidden") String error,
        @Schema(example = "/users/me") String path) {
}
