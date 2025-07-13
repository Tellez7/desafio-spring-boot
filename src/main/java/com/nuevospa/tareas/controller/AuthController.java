package com.nuevospa.tareas.controller;

import com.nuevospa.tareas.model.AuthRequest;
import com.nuevospa.tareas.model.AuthResponse;
import com.nuevospa.tareas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//TODO: colocar api
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return auth.authenticate(req.username(), req.password());
    }
}