package com.example.ricceta_mia.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(@Schema(description = "Token JWT. Cole somente este valor no botão Authorize do Swagger.") String token) {
}
