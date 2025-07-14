package com.nuevospa.tasks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para /auth/login.
 */
//TODO: check
public record AuthRequest(

        @NotBlank
        @JsonProperty("username")
        String username,

        @NotBlank
        @JsonProperty("password")
        String password
) {
}
