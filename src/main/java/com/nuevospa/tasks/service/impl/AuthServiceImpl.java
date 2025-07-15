package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.model.AuthResponseDto;
import com.nuevospa.tasks.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtServiceImpl jwtService;

    @Override
    public AuthResponseDto authenticate(String username, String rawPassword) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponseDto(token, jwtService.extractExpiration(token));
    }
}
