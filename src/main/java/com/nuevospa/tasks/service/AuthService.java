package com.nuevospa.tasks.service;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.model.AuthRequest;
import com.nuevospa.tasks.model.AuthResponse;
import com.nuevospa.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    /* --------------------------------------------------------------------
       AUTENTICAR (login)
       ------------------------------------------------------------------ */
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

    /* --------------------------------------------------------------------
       REGISTRAR nuevo usuario (opcional, no expuesto en el desafío)
       ------------------------------------------------------------------ */
    public void register(AuthRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            //TODO: check message
            throw new IllegalArgumentException("El usuario ya existe");
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(req.username());
        entity.setPassword(encoder.encode(req.password()));
        entity.setRole("ROLE_USER");
        userRepo.save(entity);
    }
}
