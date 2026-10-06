package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.response.LikesResponse;
import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.entity.Like;
import com.example.ricceta_mia.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;
    private final UserService userService;

    public LikeService(LikeRepository likeRepository, PostService postService, UserService userService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public LikesResponse like(Long postId, Long currentUserId) {
        Post post = postService.getVisiblePost(postId, currentUserId);

        // a tabela tem UNIQUE (post_id, user_id): reativa a curtida removida em vez de inserir outra
        Like like = likeRepository.findByPostIdAndUserId(postId, currentUserId).orElseGet(() -> {
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUser(userService.getActiveUser(currentUserId));
            return newLike;
        });
        like.setDeletedAt(null);
        likeRepository.save(like);
        return count(postId, currentUserId);
    }

    @Transactional
    public LikesResponse unlike(Long postId, Long currentUserId) {
        postService.getVisiblePost(postId, currentUserId);
        likeRepository.findByPostIdAndUserId(postId, currentUserId)
                .filter(like -> like.getDeletedAt() == null)
                .ifPresent(like -> {
                    like.setDeletedAt(LocalDateTime.now());
                    likeRepository.save(like);
                });
        return count(postId, currentUserId);
    }

    @Transactional(readOnly = true)
    public LikesResponse count(Long postId, Long currentUserId) {
        postService.getVisiblePost(postId, currentUserId);
        return new LikesResponse(postId,
                likeRepository.countByPostIdAndDeletedAtIsNull(postId),
                likeRepository.existsByPostIdAndUserIdAndDeletedAtIsNull(postId, currentUserId));
    }
}
