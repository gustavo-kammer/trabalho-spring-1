package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.doc.UserControllerDoc;

import com.example.ricceta_mia.config.JWTUserData;
import com.example.ricceta_mia.dto.request.UpdateUserRequest;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.dto.response.PublicUserResponse;
import com.example.ricceta_mia.dto.response.UserResponse;
import com.example.ricceta_mia.service.FavoriteService;
import com.example.ricceta_mia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements UserControllerDoc {

    private final UserService userService;
    private final FavoriteService favoriteService;

    public UserController(UserService userService, FavoriteService favoriteService) {
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<PublicUserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(userService.findMe(currentUser.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicUserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal JWTUserData currentUser,
                                               @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(currentUser.userId(), request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal JWTUserData currentUser) {
        userService.delete(currentUser.userId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<PostResponse>> myFavorites(@AuthenticationPrincipal JWTUserData currentUser) {
        return ResponseEntity.ok(favoriteService.findAllByUser(currentUser.userId()));
    }

}
