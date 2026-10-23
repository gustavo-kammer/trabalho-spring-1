package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.enums.PostStatus;
import com.example.ricceta_mia.enums.RecipeCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(Long id, String title, UserSummaryResponse author, String photoUrl, String description,
                           String ingredients, String instructions, List<RecipeCategory> categories,
                           PostStatus status,
                           @Schema(description = "Total de curtidas ativas", example = "12") long totalLikes,
                           @Schema(description = "Se o usuário autenticado curtiu a receita") boolean likedByMe,
                           @Schema(description = "Se o usuário autenticado favoritou a receita") boolean favoritedByMe,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static PostResponse from(Post post, long totalLikes, boolean likedByMe, boolean favoritedByMe) {
        return new PostResponse(post.getId(), post.getTitle(), UserSummaryResponse.from(post.getUser()),
                post.getPhotoUrl(), post.getDescription(), post.getIngredients(), post.getInstructions(),
                List.of(post.getCategories()), post.getStatus(), totalLikes, likedByMe, favoritedByMe,
                post.getCreatedAt(), post.getUpdatedAt());
    }
}
