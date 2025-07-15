package com.nuevospa.tasks.util;

import io.swagger.v3.oas.annotations.media.Schema;

public enum TaskStatus {
    @Schema(description = "TODO") TODO,
    @Schema(description = "IN_PROGRESS") IN_PROGRESS,
    @Schema(description = "DONE") DONE;
}
