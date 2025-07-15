package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.model.AuthResponse;
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

    /* --------------------------------------------------------------------
       AUTENTICAR (login)
       ------------------------------------------------------------------ */
    @Override
    public AuthResponse authenticate(String username, String rawPassword) {

        // 1) Delegamos la verificación de credenciales al AuthenticationManager
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));

        // 2) Al llegar aquí las credenciales son correctas
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        // 3) Generamos el JWT
        String token = jwtService.generateToken(userDetails);

        // 4) Devolvemos la respuesta al controlador
        return new AuthResponse(token, jwtService.extractExpiration(token));
    }
}
