package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> favorite(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        favoriteService.favorite(postId, currentUser.userId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfavorite(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        favoriteService.unfavorite(postId, currentUser.userId());
        return ResponseEntity.noContent().build();
    }

}
