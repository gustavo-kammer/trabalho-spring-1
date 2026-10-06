package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByUserIdAndUserFollowedId(Long userId, Long userFollowedId);

    // quem segue o usuário
    List<Follower> findAllByUserFollowedIdAndUnfollowedAtIsNull(Long userFollowedId);

    // quem o usuário segue
    List<Follower> findAllByUserIdAndUnfollowedAtIsNull(Long userId);

}
