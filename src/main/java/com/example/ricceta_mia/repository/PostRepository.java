package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Post;
import com.example.ricceta_mia.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndStatusNot(Long id, PostStatus status);

    List<Post> findAllByStatusOrderByCreatedAtDesc(PostStatus status);

    List<Post> findAllByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PostStatus status);

    @Query(value = "SELECT * FROM posts p " +
            "WHERE p.status = 'ACTIVE' AND CAST(:category AS recipe_category) = ANY (p.categories) " +
            "ORDER BY p.created_at DESC", nativeQuery = true)
    List<Post> findAllActiveByCategory(@Param("category") String category);

}
