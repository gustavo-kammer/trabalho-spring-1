package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);

    List<Comment> findAllByPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long postId);

}
