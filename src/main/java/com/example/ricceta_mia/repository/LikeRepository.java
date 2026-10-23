package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    long countByPostIdAndDeletedAtIsNull(Long postId);

    boolean existsByPostIdAndUserIdAndDeletedAtIsNull(Long postId, Long userId);

    // SQL nativo porque "Like" é palavra reservada no JPQL; cada linha traz [post_id, total]
    @Query(value = "SELECT l.post_id, COUNT(*) FROM likes l " +
            "WHERE l.post_id IN (:postIds) AND l.deleted_at IS NULL GROUP BY l.post_id", nativeQuery = true)
    List<Object[]> countActiveByPostIds(@Param("postIds") Collection<Long> postIds);

    @Query(value = "SELECT l.post_id FROM likes l " +
            "WHERE l.post_id IN (:postIds) AND l.user_id = :userId AND l.deleted_at IS NULL", nativeQuery = true)
    List<Long> findPostIdsLikedByUser(@Param("postIds") Collection<Long> postIds, @Param("userId") Long userId);

}
