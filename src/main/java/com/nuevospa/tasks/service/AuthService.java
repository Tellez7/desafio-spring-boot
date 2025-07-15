package com.nuevospa.tasks.service;

import com.nuevospa.tasks.model.AuthResponseDto;

public interface AuthService {

    AuthResponseDto authenticate(String username, String rawPassword);
}
