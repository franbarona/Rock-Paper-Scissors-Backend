package com.rockpaperscissors.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

@Component
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JwtKeyProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}