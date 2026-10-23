package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import com.example.ricceta_mia.dto.response.LikesResponse;
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

@Tag(name = "Curtidas", description = "Curtidas em receitas visíveis para o usuário autenticado.")
@SecurityRequirement(name = OpenAPIConfig.SECURITY_SCHEME_NAME)
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado",
                content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
})
public interface LikeControllerDoc {

    @Operation(summary = "Consulta curtidas",
            description = "Retorna o total de curtidas e se o usuário autenticado curtiu a receita.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikesResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<LikesResponse> count(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Curte uma receita",
            description = "Adiciona ou reativa a curtida. Repetir a operação não duplica a curtida.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikesResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<LikesResponse> like(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Remove minha curtida",
            description = "Remove a curtida, se existir, e retorna a contagem atualizada. A receita precisa estar visível.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LikesResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<LikesResponse> unlike(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);
}
