package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserRequest(
        @Schema(description = "Nome do usuário", example = "Maria")
        @NotEmpty(message = "Campo nome é obrigatório") @Size(max = 150) String firstName,
        @Schema(description = "Sobrenome opcional", example = "Silva")
        @Size(max = 150) String lastName,
        @Schema(description = "E-mail único de acesso", example = "maria@example.com")
        @NotEmpty(message = "Campo e-mail é obrigatório") @Email(message = "E-mail inválido") @Size(max = 150) String email,
        @Schema(description = "Senha de 8 a 72 caracteres", example = "Exemplo123!", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotEmpty(message = "Campo senha é obrigatório") @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres") String password,
        @Schema(description = "Apelido opcional e único", example = "maria_cozinha")
        @Size(max = 100) String nickname) {



}
