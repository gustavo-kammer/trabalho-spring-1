package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.entity.Favorite;
import com.example.ricceta_mia.enums.PostStatus;
import com.example.ricceta_mia.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostService postService;
    private final UserService userService;

    public FavoriteService(FavoriteRepository favoriteRepository, PostService postService, UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public void favorite(Long postId, Long currentUserId) {
        Post post = postService.getVisiblePost(postId, currentUserId);

        // a tabela tem UNIQUE (post_id, user_id): reativa o favorito removido em vez de inserir outro
        Favorite favorite = favoriteRepository.findByPostIdAndUserId(postId, currentUserId).orElseGet(() -> {
            Favorite newFavorite = new Favorite();
            newFavorite.setPost(post);
            newFavorite.setUser(userService.getActiveUser(currentUserId));
            return newFavorite;
        });
        favorite.setDeletedAt(null);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void unfavorite(Long postId, Long currentUserId) {
        favoriteRepository.findByPostIdAndUserId(postId, currentUserId)
                .filter(favorite -> favorite.getDeletedAt() == null)
                .ifPresent(favorite -> {
                    favorite.setDeletedAt(LocalDateTime.now());
                    favoriteRepository.save(favorite);
                });
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAllByUser(Long userId) {
        return favoriteRepository.findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId).stream()
                .map(Favorite::getPost)
                .filter(post -> post.getStatus() == PostStatus.ACTIVE)
                .map(PostResponse::from)
                .toList();
    }
}
