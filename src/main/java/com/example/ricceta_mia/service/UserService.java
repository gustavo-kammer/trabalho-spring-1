package com.example.ricceta_mia.service;

import com.example.ricceta_mia.dto.request.UpdateUserRequest;
import com.example.ricceta_mia.dto.response.UserResponse;
import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.enums.RecipeCategory;
import com.example.ricceta_mia.enums.UserStatus;
import com.example.ricceta_mia.exception.ConflictException;
import com.example.ricceta_mia.exception.ResourceNotFoundException;
import com.example.ricceta_mia.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAllByStatusOrderByFirstNameAsc(UserStatus.ACTIVE).stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(getActiveUser(id));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = getActiveUser(id);

        if (request.nickname() != null && userRepository.existsByNicknameAndIdNot(request.nickname(), id)) {
            throw new ConflictException("Nickname já está em uso");
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setNickname(request.nickname());
        user.setCityRegion(request.cityRegion());
        user.setBio(request.bio());
        if (request.preferredCategories() != null) {
            user.setPreferredCategories(request.preferredCategories().toArray(new RecipeCategory[0]));
        }
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getActiveUser(id);
        user.setStatus(UserStatus.DELETED);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User getActiveUser(Long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
