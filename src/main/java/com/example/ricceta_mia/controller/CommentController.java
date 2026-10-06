package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.dto.request.CommentRequest;
import com.example.ricceta_mia.dto.response.CommentResponse;
import com.example.ricceta_mia.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser,
                                                  @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(postId, currentUser.userId(), request));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> findAllByPost(@PathVariable Long postId, @AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(commentService.findAllByPost(postId, currentUser.userId()));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser,
                                                  @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.update(id, currentUser.userId(), request));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal JWTUserData currentUser) {
        commentService.delete(id, currentUser.userId());
        return ResponseEntity.noContent().build();
    }

}
