package com.nuevospa.tareas.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos de entrada para /auth/login.
 */
public record AuthRequest(

        @NotBlank
        @JsonProperty("username")
        String username,

        @NotBlank
        @JsonProperty("password")
        String password
) {
}
