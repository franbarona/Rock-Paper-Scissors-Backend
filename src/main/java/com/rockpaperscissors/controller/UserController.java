package com.rockpaperscissors.controller;

import com.rockpaperscissors.dto.request.UpdateUserRequest;
import com.rockpaperscissors.dto.response.ApiResponse;
import com.rockpaperscissors.dto.response.UserResponse;
import com.rockpaperscissors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserResponse user = userService.getCurrentUser(email);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateUserRequest updateRequest) {
        String email = authentication.getName();
        UserResponse user = userService.updateCurrentUser(email, updateRequest);
        return ResponseEntity.ok(ApiResponse.success(user, "User updated successfully"));
    }
}