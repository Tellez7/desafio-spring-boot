package com.nuevospa.tasks.util;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Role {
    @Schema(description = "Administrador") ADMIN,
    @Schema(description = "Usuario estándar") USER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
