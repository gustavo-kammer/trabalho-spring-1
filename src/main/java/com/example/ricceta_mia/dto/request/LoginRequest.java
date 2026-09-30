package com.example.ricceta_mia.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "E-mail é obrigatório") String email, @NotEmpty(message = "Senha é obrigatória") String password) {



}
