package com.example.ricceta_mia.service;

import com.example.ricceta_mia.config.TokenConfig;
import com.example.ricceta_mia.dto.request.LoginRequest;
import com.example.ricceta_mia.dto.request.RegisterUserRequest;
import com.example.ricceta_mia.dto.response.LoginResponse;
import com.example.ricceta_mia.dto.response.RegisterUserResponse;
import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.exception.ConflictException;
import com.example.ricceta_mia.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User) authentication.getPrincipal();
        return new LoginResponse(tokenConfig.generateToken(user));
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("E-mail já cadastrado");
        }
        if (request.nickname() != null && userRepository.existsByNickname(request.nickname())) {
            throw new ConflictException("Nickname já está em uso");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setNickname(request.nickname());
        newUser.setEmail(request.email());
        userRepository.save(newUser);
        return new RegisterUserResponse(newUser.getId(), newUser.getFirstName(), newUser.getEmail());
    }
}
