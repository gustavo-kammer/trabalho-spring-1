package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.request.PostRequest;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.enums.PostStatus;
import com.example.ricceta_mia.enums.RecipeCategory;
import com.example.ricceta_mia.exception.BusinessException;
import com.example.ricceta_mia.exception.ForbiddenException;
import com.example.ricceta_mia.exception.ResourceNotFoundException;
import com.example.ricceta_mia.repository.FavoriteRepository;
import com.example.ricceta_mia.repository.LikeRepository;
import com.example.ricceta_mia.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;

    public PostService(PostRepository postRepository, UserService userService,
                       LikeRepository likeRepository, FavoriteRepository favoriteRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public PostResponse create(Long userId, PostRequest request) {
        Post post = new Post();
        post.setUser(userService.getActiveUser(userId));
        applyRequest(post, request);
        return toResponse(postRepository.save(post), userId);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAll(Long userId, RecipeCategory category, Long currentUserId) {
        List<Post> posts;
        if (category != null) {
            posts = postRepository.findAllActiveByCategory(category.name());
            if (userId != null) {
                posts = posts.stream().filter(post -> post.getUser().getId().equals(userId)).toList();
            }
        } else if (userId != null) {
            posts = postRepository.findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, PostStatus.ACTIVE);
        } else {
            posts = postRepository.findAllByStatusOrderByCreatedAtDesc(PostStatus.ACTIVE);
        }
        return toResponses(posts, currentUserId);
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id, Long currentUserId) {
        return toResponse(getVisiblePost(id, currentUserId), currentUserId);
    }

    @Transactional
    public PostResponse update(Long id, Long currentUserId, PostRequest request) {
        Post post = getOwnPost(id, currentUserId);
        applyRequest(post, request);
        return toResponse(postRepository.saveAndFlush(post), currentUserId);
    }

    @Transactional
    public PostResponse updateStatus(Long id, Long currentUserId, PostStatus status) {
        if (status == PostStatus.DELETED) {
            throw new BusinessException("Use DELETE /posts/{id} para remover o post");
        }
        Post post = getOwnPost(id, currentUserId);
        post.setStatus(status);
        return toResponse(postRepository.saveAndFlush(post), currentUserId);
    }

    // busca curtidas e favoritos de todas as receitas de uma vez, em vez de uma consulta por receita
    public List<PostResponse> toResponses(List<Post> posts, Long currentUserId) {
        if (posts.isEmpty()) {
            return List.of();
        }
        List<Long> postIds = posts.stream().map(Post::getId).toList();

        Map<Long, Long> totalLikes = new HashMap<>();
        for (Object[] row : likeRepository.countActiveByPostIds(postIds)) {
            totalLikes.put(((Number) row[0]).longValue(), ((Number) row[1]).longValue());
        }
        Set<Long> liked = new HashSet<>(likeRepository.findPostIdsLikedByUser(postIds, currentUserId));
        Set<Long> favorited = new HashSet<>(favoriteRepository.findPostIdsFavoritedByUser(postIds, currentUserId));

        return posts.stream()
                .map(post -> PostResponse.from(post, totalLikes.getOrDefault(post.getId(), 0L),
                        liked.contains(post.getId()), favorited.contains(post.getId())))
                .toList();
    }

    private PostResponse toResponse(Post post, Long currentUserId) {
        return toResponses(List.of(post), currentUserId).getFirst();
    }

    @Transactional
    public void delete(Long id, Long currentUserId) {
        Post post = getOwnPost(id, currentUserId);
        post.setStatus(PostStatus.DELETED);
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    // posts arquivados só ficam visíveis para o próprio autor
    public Post getVisiblePost(Long id, Long currentUserId) {
        Post post = postRepository.findByIdAndStatusNot(id, PostStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado"));
        if (post.getStatus() == PostStatus.ARCHIVED && !post.getUser().getId().equals(currentUserId)) {
            throw new ResourceNotFoundException("Post não encontrado");
        }
        return post;
    }

    private Post getOwnPost(Long id, Long currentUserId) {
        Post post = getVisiblePost(id, currentUserId);
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("Você não pode alterar este post");
        }
        return post;
    }

    private void applyRequest(Post post, PostRequest request) {
        post.setTitle(request.title());
        post.setPhotoUrl(request.photoUrl());
        post.setDescription(request.description());
        post.setIngredients(request.ingredients());
        post.setInstructions(request.instructions());
        post.setCategories(request.categories() == null
                ? new RecipeCategory[0]
                : request.categories().toArray(new RecipeCategory[0]));
    }
}
