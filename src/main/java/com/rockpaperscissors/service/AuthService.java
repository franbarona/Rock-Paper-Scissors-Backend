package com.rockpaperscissors.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rockpaperscissors.dto.request.LoginRequest;
import com.rockpaperscissors.dto.request.RegisterRequest;
import com.rockpaperscissors.dto.response.LoginResponse;
import com.rockpaperscissors.dto.response.RegisterResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.exception.InvalidCredentialsException;
import com.rockpaperscissors.exception.UserAlreadyExistsException;
import com.rockpaperscissors.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StatisticsService statisticsService;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("username", request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("email", request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());
        statisticsService.updateUserStatistics(savedUser, null); // Initialize statistics
        return new RegisterResponse(token, savedUser.getUsername(), savedUser.getEmail(), savedUser.getId());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getUsername());
        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getId());
    }
}
