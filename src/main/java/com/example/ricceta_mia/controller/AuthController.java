package com.example.ricceta_mia.controller;

import com.example.ricceta_mia.config.TokenConfig;
import com.example.ricceta_mia.dto.request.LoginRequest;
import com.example.ricceta_mia.dto.request.RegisterUserRequest;
import com.example.ricceta_mia.dto.response.LoginResponse;
import com.example.ricceta_mia.dto.response.RegisterUserResponse;
import com.example.ricceta_mia.entity.User;
import com.example.ricceta_mia.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request){
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        if (request.nickname() != null && userRepository.existsByNickname(request.nickname())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nickname já está em uso");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode( request.password()));
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setNickname(request.nickname());
        newUser.setEmail(request.email());
        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getId(), newUser.getFirstName(), newUser.getEmail()));
    }

}
