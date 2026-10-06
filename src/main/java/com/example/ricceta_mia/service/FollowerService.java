package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.response.UserSummaryResponse;
import com.example.ricceta_mia.entity.Follower;
import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.enums.UserStatus;
import com.example.ricceta_mia.exception.BusinessException;
import com.example.ricceta_mia.repository.FollowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserService userService;

    public FollowerService(FollowerRepository followerRepository, UserService userService) {
        this.followerRepository = followerRepository;
        this.userService = userService;
    }

    @Transactional
    public void follow(Long currentUserId, Long userToFollowId) {
        if (currentUserId.equals(userToFollowId)) {
            throw new BusinessException("Você não pode seguir a si mesmo");
        }
        User userToFollow = userService.getActiveUser(userToFollowId);

        // a tabela tem UNIQUE (user_id, user_followed_id): reativa o follow antigo em vez de inserir outro
        Follower follower = followerRepository.findByUserIdAndUserFollowedId(currentUserId, userToFollowId)
                .orElseGet(() -> {
                    Follower newFollower = new Follower();
                    newFollower.setUser(userService.getActiveUser(currentUserId));
                    newFollower.setUserFollowed(userToFollow);
                    return newFollower;
                });
        follower.setUnfollowedAt(null);
        followerRepository.save(follower);
    }

    @Transactional
    public void unfollow(Long currentUserId, Long userFollowedId) {
        followerRepository.findByUserIdAndUserFollowedId(currentUserId, userFollowedId)
                .filter(follower -> follower.getUnfollowedAt() == null)
                .ifPresent(follower -> {
                    follower.setUnfollowedAt(LocalDateTime.now());
                    followerRepository.save(follower);
                });
    }

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> findFollowers(Long userId) {
        userService.getActiveUser(userId);
        return followerRepository.findAllByUserFollowedIdAndUnfollowedAtIsNull(userId).stream()
                .map(Follower::getUser)
                .filter(user -> user.getStatus() == UserStatus.ACTIVE)
                .map(UserSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> findFollowing(Long userId) {
        userService.getActiveUser(userId);
        return followerRepository.findAllByUserIdAndUnfollowedAtIsNull(userId).stream()
                .map(Follower::getUserFollowed)
                .filter(user -> user.getStatus() == UserStatus.ACTIVE)
                .map(UserSummaryResponse::from)
                .toList();
    }
}
