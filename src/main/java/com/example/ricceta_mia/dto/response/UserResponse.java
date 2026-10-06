package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.enums.RecipeCategory;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(Long id, String firstName, String lastName, String email, String nickname,
                           String cityRegion, String bio, List<RecipeCategory> preferredCategories,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getNickname(), user.getCityRegion(), user.getBio(), List.of(user.getPreferredCategories()),
                user.getCreatedAt(), user.getUpdatedAt());
    }
}
