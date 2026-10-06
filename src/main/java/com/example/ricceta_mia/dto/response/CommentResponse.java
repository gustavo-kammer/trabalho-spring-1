package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, Long postId, UserSummaryResponse author, String content,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getPost().getId(), UserSummaryResponse.from(comment.getUser()),
                comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
