package com.example.ricceta_mia.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
