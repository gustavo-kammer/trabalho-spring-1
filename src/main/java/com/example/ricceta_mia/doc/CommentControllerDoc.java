package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import com.example.ricceta_mia.dto.request.CommentRequest;
import com.example.ricceta_mia.dto.response.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

@Tag(name = "Comentários", description = "Comentários em receitas e regras de autoria.")
@SecurityRequirement(name = OpenAPIConfig.SECURITY_SCHEME_NAME)
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado",
                content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Operação permitida somente ao autor do registro",
                content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
})
public interface CommentControllerDoc {

    @Operation(summary = "Adiciona um comentário",
            description = "Adiciona um comentário a uma receita visível para o usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<CommentResponse> create(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) CommentRequest request);

    @Operation(summary = "Lista comentários de uma receita",
            description = "Retorna comentários não excluídos, do mais antigo para o mais recente, de uma receita visível.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<List<CommentResponse>> findAllByPost(@Parameter(description = "Identificador da receita", example = "1") Long postId,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Edita um comentário",
            description = "Somente o autor do comentário pode editar seu conteúdo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<CommentResponse> update(@Parameter(description = "Identificador do comentário", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) CommentRequest request);

    @Operation(summary = "Remove um comentário",
            description = "O autor do comentário ou da receita pode removê-lo. A exclusão preserva o registro no banco.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> delete(@Parameter(description = "Identificador do comentário", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser);
}
