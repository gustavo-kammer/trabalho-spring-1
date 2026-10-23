package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.enums.RecipeCategory;

import java.time.LocalDateTime;
import java.util.List;

// perfil visto por outros usuários: igual ao UserResponse, mas sem o e-mail
public record PublicUserResponse(Long id, String firstName, String lastName, String nickname,
                                 String cityRegion, String bio, List<RecipeCategory> preferredCategories,
                                 LocalDateTime createdAt) {

    public static PublicUserResponse from(User user) {
        return new PublicUserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getNickname(),
                user.getCityRegion(), user.getBio(), List.of(user.getPreferredCategories()), user.getCreatedAt());
    }
}
