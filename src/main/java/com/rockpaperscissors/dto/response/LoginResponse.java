package com.rockpaperscissors.dto.response;

public class LoginResponse {

    private String token;
    private String username;
    private String email;
    private Long id;

    public LoginResponse(String token, String username, String email, Long id) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
