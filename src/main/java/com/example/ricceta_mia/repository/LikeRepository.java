package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    long countByPostIdAndDeletedAtIsNull(Long postId);

    boolean existsByPostIdAndUserIdAndDeletedAtIsNull(Long postId, Long userId);

}
