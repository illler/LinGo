package com.example.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.backend.config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtServiceTest {

    private JwtService jwtService;

    @Value("${application.security.jwt.secretKey}")
    private String secretKey;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        assertTrue(token != null && !token.isEmpty());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject());
    }

    @Test
    public void testIsTokenValid_ValidToken_ReturnsTrue() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenValid_InvalidToken_ReturnsFalse() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("USER")
                .build();

        String token = "invalidToken"; // Указываем недействительный токен
        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenValid_ExpiredToken_ReturnsFalse() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        // Имитируем истекший токен, установив дату истечения в прошлом
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        claims.setExpiration(new Date(System.currentTimeMillis() - 3600000)); // 1 час назад
        token = Jwts.builder()
                .setClaims(claims)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


//