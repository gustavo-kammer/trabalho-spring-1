package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.dto.request.PostRequest;
import com.example.ricceta_mia.dto.request.UpdatePostStatusRequest;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.enums.RecipeCategory;
import com.example.ricceta_mia.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@AuthenticationPrincipal JWTUserData currentUser,
                                               @Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(currentUser.userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll(@RequestParam(required = false) Long userId,
                                                      @RequestParam(required = false) RecipeCategory category) {
        return ResponseEntity.ok(postService.findAll(userId, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(postService.findById(id, currentUser.userId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser,
                                               @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.update(id, currentUser.userId(), request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PostResponse> updateStatus(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser,
                                                     @Valid @RequestBody UpdatePostStatusRequest request) {
        return ResponseEntity.ok(postService.updateStatus(id, currentUser.userId(), request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser) {
        postService.delete(id, currentUser.userId());
        return ResponseEntity.noContent().build();
    }

}
