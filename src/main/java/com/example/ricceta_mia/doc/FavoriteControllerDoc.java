package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "Favoritos", description = "Marcações de receitas favoritas do usuário autenticado.")
@SecurityRequirement(name = OpenAPIConfig.SECURITY_SCHEME_NAME)
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Acesso negado. Token ausente ou inválido retorna application/json; restrições de autoria retornam application/problem+json.",
                content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = SecurityErrorResponse.class)),
                        @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
                }),
        @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
})
public interface FavoriteControllerDoc {

    @Operation(summary = "Favorita uma receita",
            description = "Adiciona ou reativa um favorito em uma receita visível. Repetir a operação não duplica o favorito.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> favorite(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Remove um favorito",
            description = "Remove o favorito se existir. Retorna 204 também quando não houver favorito para o identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content)
    })
    ResponseEntity<Void> unfavorite(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);
}
