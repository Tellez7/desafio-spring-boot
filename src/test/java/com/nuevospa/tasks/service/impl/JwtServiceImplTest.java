package com.nuevospa.tasks.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static com.nuevospa.tasks.util.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtServiceImplTest {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    private UserDetails user = org.springframework.security.core.userdetails.User
            .withUsername("user")
            .password("pass")
            .roles("USER")
            .build();

    private String token;

    @Autowired
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        user = org.springframework.security.core.userdetails.User
                .withUsername("esteban")
                .password("pass")
                .roles(ADMIN.name())
                .build();

        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlc3RlYmFuIiwiaWF0IjoxNzUzNzI3OTI3LCJleHAiOjIwNjkwODc5Mjd9.TgilIglBuu2KZ5cnsgX1Jl2N9UF8OHHFUaC__TbwTq4";
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void isTokenValid() {
        boolean isTokenValid = jwtService.isTokenValid(token, user);
        assertTrue(isTokenValid);
    }

    @Test
    void extractExpiration() {
    }
}