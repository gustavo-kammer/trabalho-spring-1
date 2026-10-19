package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePostStatusRequest(
        @Schema(description = "Use ACTIVE para reativar ou ARCHIVED para arquivar. DELETED retorna 400; a exclusão usa DELETE /posts/{id}.", example = "ARCHIVED")
        @NotNull(message = "Campo status é obrigatório") PostStatus status) {
}
