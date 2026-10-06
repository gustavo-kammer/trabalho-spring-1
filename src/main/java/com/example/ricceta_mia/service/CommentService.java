package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.request.CommentRequest;
import com.example.ricceta_mia.dto.response.CommentResponse;
import com.example.ricceta_mia.entity.Comment;
import com.example.ricceta_mia.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public CommentResponse create(Long postId, Long currentUserId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setPost(postService.getVisiblePost(postId, currentUserId));
        comment.setUser(userService.getActiveUser(currentUserId));
        comment.setContent(request.content());
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByPost(Long postId, Long currentUserId) {
        postService.getVisiblePost(postId, currentUserId);
        return commentRepository.findAllByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse update(Long id, Long currentUserId, CommentRequest request) {
        Comment comment = getOwnComment(id, currentUserId);
        comment.setContent(request.content());
        return CommentResponse.from(commentRepository.saveAndFlush(comment));
    }

    @Transactional
    public void delete(Long id, Long currentUserId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentário não encontrado"));

        // o autor do comentário ou o autor do post podem remover
        boolean isCommentAuthor = comment.getUser().getId().equals(currentUserId);
        boolean isPostAuthor = comment.getPost().getUser().getId().equals(currentUserId);
        if (!isCommentAuthor && !isPostAuthor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode remover este comentário");
        }

        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    private Comment getOwnComment(Long id, Long currentUserId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentário não encontrado"));
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode alterar este comentário");
        }
        return comment;
    }
}
