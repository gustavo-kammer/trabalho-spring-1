package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByPostIdAndUserId(Long postId, Long userId);

    List<Favorite> findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

}
