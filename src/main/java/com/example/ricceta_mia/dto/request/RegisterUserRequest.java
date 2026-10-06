package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotEmpty(message = "Campo nome é obrigatório") @Size(max = 150) String firstName,
                                  @Size(max = 150) String lastName,
                                  @NotEmpty(message = "Campo e-mail é obrigatório") @Email(message = "E-mail inválido") @Size(max = 150) String email,
                                  @NotEmpty(message = "Campo senha é obrigatório") String password,
                                  @Size(max = 100) String nickname) {



}
