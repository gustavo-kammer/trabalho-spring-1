package com.example.ricceta_mia.dto.response;

import com.example.ricceta_mia.entity.User;

public record UserSummaryResponse(Long id, String firstName, String lastName, String nickname) {

    public static UserSummaryResponse from(User user) {
        return new UserSummaryResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getNickname());
    }
}
