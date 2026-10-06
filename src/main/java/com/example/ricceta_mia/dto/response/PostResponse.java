package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.enums.PostStatus;
import com.example.ricceta_mia.enums.RecipeCategory;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(Long id, String title, UserSummaryResponse author, String photoUrl, String description,
                           String ingredients, String instructions, List<RecipeCategory> categories,
                           PostStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), UserSummaryResponse.from(post.getUser()),
                post.getPhotoUrl(), post.getDescription(), post.getIngredients(), post.getInstructions(),
                List.of(post.getCategories()), post.getStatus(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
