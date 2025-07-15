package com.nuevospa.tasks.controller;

import com.nuevospa.tasks.model.AuthRequest;
import com.nuevospa.tasks.model.AuthResponse;
import com.nuevospa.tasks.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//TODO: colocar api
@RequestMapping("${api.path-auth}")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return auth.authenticate(req.username(), req.password());
    }
}