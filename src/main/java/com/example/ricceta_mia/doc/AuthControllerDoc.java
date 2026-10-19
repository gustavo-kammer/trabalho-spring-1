package com.example.ricceta_mia.doc;

import com.example.ricceta_mia.dto.request.LoginRequest;
import com.example.ricceta_mia.dto.response.LoginResponse;
import com.example.ricceta_mia.dto.request.RegisterUserRequest;
import com.example.ricceta_mia.dto.response.RegisterUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = "Cadastro de usuários e obtenção do token JWT.")
@ApiResponses({
        @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
})
public interface AuthControllerDoc {

    @Operation(summary = "Realiza o login",
            description = "Autentica com e-mail e senha e retorna o token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<LoginResponse> login(@RequestBody(description = "Dados da operação", required = true) LoginRequest request);

    @Operation(summary = "Cadastra um usuário",
            description = "Cria um usuário. E-mail e nickname devem ser únicos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida pelas regras de negócio",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com dados já cadastrados",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<RegisterUserResponse> register(@RequestBody(description = "Dados da operação", required = true) RegisterUserRequest request);
}
