package com.nuevospa.tasks.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskStatusDto {

    private Long id;
    @NotBlank(message = "El campo name no esta presente")
    private String name;
}