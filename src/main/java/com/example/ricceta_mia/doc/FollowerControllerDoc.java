package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import com.example.ricceta_mia.dto.response.UserSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Seguidores", description = "Relacionamentos entre usuários ativos.")
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
public interface FollowerControllerDoc {

    @Operation(summary = "Segue um usuário",
            description = "Segue um usuário ativo ou reativa o vínculo. Não permite seguir a si mesmo e não duplica vínculos existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> follow(@Parameter(description = "Identificador do usuário", example = "1") Long userId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Deixa de seguir um usuário",
            description = "Remove o vínculo se existir. Retorna 204 também quando não houver vínculo para o identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content)
    })
    ResponseEntity<Void> unfollow(@Parameter(description = "Identificador do usuário", example = "1") Long userId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Lista seguidores",
            description = "Lista os usuários ativos que seguem o usuário informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserSummaryResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<List<UserSummaryResponse>> followers(@Parameter(description = "Identificador do usuário", example = "1") Long userId);

    @Operation(summary = "Lista usuários seguidos",
            description = "Lista os usuários ativos seguidos pelo usuário informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserSummaryResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<List<UserSummaryResponse>> following(@Parameter(description = "Identificador do usuário", example = "1") Long userId);
}
