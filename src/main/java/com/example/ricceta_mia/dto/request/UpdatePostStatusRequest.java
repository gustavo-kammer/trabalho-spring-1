package com.example.ricceta_mia.dto.request;

import com.example.ricceta_mia.enums.PostStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePostStatusRequest(@NotNull(message = "Campo status é obrigatório") PostStatus status) {
}
