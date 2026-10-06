package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.request.PostRequest;
import com.example.ricceta_mia.dto.response.PostResponse;
import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.enums.PostStatus;
import com.example.ricceta_mia.enums.RecipeCategory;
import com.example.ricceta_mia.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public PostResponse create(Long userId, PostRequest request) {
        Post post = new Post();
        post.setUser(userService.getActiveUser(userId));
        applyRequest(post, request);
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAll(Long userId, RecipeCategory category) {
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
        return posts.stream().map(PostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id, Long currentUserId) {
        return PostResponse.from(getVisiblePost(id, currentUserId));
    }

    @Transactional
    public PostResponse update(Long id, Long currentUserId, PostRequest request) {
        Post post = getOwnPost(id, currentUserId);
        applyRequest(post, request);
        return PostResponse.from(postRepository.saveAndFlush(post));
    }

    @Transactional
    public PostResponse updateStatus(Long id, Long currentUserId, PostStatus status) {
        if (status == PostStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use DELETE /posts/{id} para remover o post");
        }
        Post post = getOwnPost(id, currentUserId);
        post.setStatus(status);
        return PostResponse.from(postRepository.saveAndFlush(post));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
        if (post.getStatus() == PostStatus.ARCHIVED && !post.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado");
        }
        return post;
    }

    private Post getOwnPost(Long id, Long currentUserId) {
        Post post = getVisiblePost(id, currentUserId);
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode alterar este post");
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
