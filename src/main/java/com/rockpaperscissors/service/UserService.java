package com.rockpaperscissors.service;

import com.rockpaperscissors.dto.request.UpdateUserRequest;
import com.rockpaperscissors.dto.response.UserResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.exception.UserAlreadyExistsException;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getCurrentUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        return mapToUserResponse(user);
    }

    public UserResponse updateCurrentUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("email", request.getEmail());
        }

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("username", request.getUsername());
        }

        // Update user details
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());

        User updatedUser = userRepository.save(user);
        log.info("User updated: {} (id: {})", updatedUser.getEmail(), updatedUser.getId().toString());

        return mapToUserResponse(updatedUser);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}