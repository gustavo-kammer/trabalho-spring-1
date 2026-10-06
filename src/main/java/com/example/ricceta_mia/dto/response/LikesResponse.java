package com.example.ricceta_mia.dto.response;

public record LikesResponse(Long postId, long total, boolean likedByMe) {
}
