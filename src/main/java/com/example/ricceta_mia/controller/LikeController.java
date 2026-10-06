package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.dto.response.LikesResponse;
import com.example.ricceta_mia.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public ResponseEntity<LikesResponse> count(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(likeService.count(postId, currentUser.userId()));
    }

    @PostMapping
    public ResponseEntity<LikesResponse> like(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(likeService.like(postId, currentUser.userId()));
    }

    @DeleteMapping
    public ResponseEntity<LikesResponse> unlike(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(likeService.unlike(postId, currentUser.userId()));
    }

}
