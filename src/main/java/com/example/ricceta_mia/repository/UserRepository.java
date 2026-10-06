package com.example.ricceta_mia.repository;

import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    List<User> findAllByStatusOrderByFirstNameAsc(UserStatus status);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByNicknameAndIdNot(String nickname, Long id);

}
