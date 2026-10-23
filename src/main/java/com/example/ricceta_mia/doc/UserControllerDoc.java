package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.dto.response.PublicUserResponse;
import com.example.ricceta_mia.dto.request.UpdateUserRequest;
import com.example.ricceta_mia.dto.response.UserResponse;
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

@Tag(name = "Usuários", description = "Consulta e manutenção de perfis e favoritos.")
@SecurityRequirement(name = OpenAPIConfig.SECURITY_SCHEME_NAME)
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado",
                content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
})
public interface UserControllerDoc {

    @Operation(summary = "Lista usuários",
            description = "Retorna os usuários ativos em ordem de nome, sem o e-mail.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PublicUserResponse.class))))
    })
    ResponseEntity<List<PublicUserResponse>> findAll();

    @Operation(summary = "Consulta meu perfil",
            description = "Retorna o perfil do usuário identificado pelo token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<UserResponse> me(@Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Consulta um usuário",
            description = "Retorna o perfil público de um usuário ativo, sem o e-mail.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PublicUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<PublicUserResponse> findById(@Parameter(description = "Identificador do usuário", example = "1") Long id);

    @Operation(summary = "Atualiza meu perfil",
            description = "Atualiza o perfil autenticado. Nickname deve ser único. Se preferredCategories for omitido, mantém as preferências atuais.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com dados já cadastrados",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<UserResponse> update(@Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) UpdateUserRequest request);

    @Operation(summary = "Remove minha conta",
            description = "Marca a conta autenticada como excluída, preservando o registro no banco.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> delete(@Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Lista minhas receitas favoritas",
            description = "Retorna somente receitas ativas marcadas como favoritas pelo usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponse.class))))
    })
    ResponseEntity<List<PostResponse>> myFavorites(@Parameter(hidden = true) JWTUserData currentUser);
}
