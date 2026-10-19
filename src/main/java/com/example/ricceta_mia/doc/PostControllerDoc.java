package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.config.OpenAPIConfig;
import com.example.ricceta_mia.dto.request.PostRequest;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.dto.request.UpdatePostStatusRequest;
import com.example.ricceta_mia.enums.RecipeCategory;
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

@Tag(name = "Receitas", description = "Publicação, consulta, edição e arquivamento de receitas.")
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
public interface PostControllerDoc {

    @Operation(summary = "Publica uma receita",
            description = "Cria uma receita ativa vinculada ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<PostResponse> create(@Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) PostRequest request);

    @Operation(summary = "Lista receitas",
            description = "Retorna receitas ativas, da mais recente para a mais antiga. Os filtros de autor e categoria podem ser combinados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<List<PostResponse>> findAll(@Parameter(description = "Filtro opcional pelo autor", example = "1") Long userId,
            @Parameter(description = "Filtro opcional pela categoria") RecipeCategory category);

    @Operation(summary = "Consulta uma receita",
            description = "Receitas arquivadas são visíveis somente ao próprio autor. Receitas excluídas ou não visíveis retornam 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<PostResponse> findById(@Parameter(description = "Identificador da receita", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser);

    @Operation(summary = "Atualiza uma receita",
            description = "Somente o autor pode editar. Se categories for omitido, a receita fica sem categorias.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<PostResponse> update(@Parameter(description = "Identificador da receita", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) PostRequest request);

    @Operation(summary = "Arquiva ou reativa uma receita",
            description = "Somente o autor pode definir ACTIVE ou ARCHIVED. DELETED é rejeitado com 400; para excluir, use DELETE /posts/{id}.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<PostResponse> updateStatus(@Parameter(description = "Identificador da receita", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser,
            @RequestBody(description = "Dados da operação", required = true) UpdatePostStatusRequest request);

    @Operation(summary = "Remove uma receita",
            description = "Somente o autor pode excluir. Marca a receita como DELETED, preservando o registro no banco.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operação concluída, sem corpo de resposta",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário, receita ou comentário não encontrado ou não visível",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> delete(@Parameter(description = "Identificador da receita", example = "1") Long id,
            @Parameter(hidden = true) JWTUserData currentUser);
}
