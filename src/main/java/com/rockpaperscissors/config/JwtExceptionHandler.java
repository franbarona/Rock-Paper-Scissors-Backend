package com.rockpaperscissors.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockpaperscissors.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.error("Authentication failed: {}", authException.getMessage());
        sendError(response, "Unauthorized: Invalid or missing authentication token",
                "AUTHENTICATION_FAILED", HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        log.error("Access denied: {}", accessDeniedException.getMessage());
        sendError(response, "Forbidden: You don't have permission to access this resource",
                "ACCESS_DENIED", HttpServletResponse.SC_FORBIDDEN);
    }

    private void sendError(HttpServletResponse response, String message, String errorCode, int status)
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);

        ApiResponse<Void> apiResponse = ApiResponse.error(message, errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}