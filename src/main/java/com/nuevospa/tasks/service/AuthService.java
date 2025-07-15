package com.nuevospa.tasks.service;

import com.nuevospa.tasks.model.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(String username, String rawPassword);
}
