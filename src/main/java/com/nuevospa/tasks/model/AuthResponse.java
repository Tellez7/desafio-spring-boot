package com.nuevospa.tasks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record AuthResponse(
        @JsonProperty("access_token") String token,
        @JsonProperty("expires_at") Date expiresAt
) {
}
