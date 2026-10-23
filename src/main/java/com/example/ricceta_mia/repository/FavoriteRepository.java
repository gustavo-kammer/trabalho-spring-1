package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByPostIdAndUserId(Long postId, Long userId);

    List<Favorite> findAllByUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId);

    @Query("SELECT f.post.id FROM Favorite f " +
            "WHERE f.post.id IN :postIds AND f.user.id = :userId AND f.deletedAt IS NULL")
    List<Long> findPostIdsFavoritedByUser(@Param("postIds") Collection<Long> postIds, @Param("userId") Long userId);

}
