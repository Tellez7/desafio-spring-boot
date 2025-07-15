package com.nuevospa.tasks.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(

        @NotBlank
        @JsonProperty("username")
        String username,

        @NotBlank
        @JsonProperty("password")
        String password
) {
}
