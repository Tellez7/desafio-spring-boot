package com.nuevospa.tasks.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String generateToken(UserDetails user, Map<String, Object> extraClaims);

    String generateToken(UserDetails user);

    boolean isTokenValid(String token, UserDetails user);

    String extractUsername(String token);

}
