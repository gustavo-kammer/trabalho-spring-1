package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.dto.response.UserSummaryResponse;
import com.example.ricceta_mia.service.FollowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@PathVariable Long userId, @AuthenticationPrincipal JWTUserData currentUser) {
        followerService.follow(currentUser.userId(), userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId, @AuthenticationPrincipal JWTUserData currentUser) {
        followerService.unfollow(currentUser.userId(), userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserSummaryResponse>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(followerService.findFollowers(userId));
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserSummaryResponse>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(followerService.findFollowing(userId));
    }

}
