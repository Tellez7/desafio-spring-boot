package com.nuevospa.tasks.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskDto {

    private Long id;
    @NotBlank(message = "El campo title no esta presente")
    private String title;
    @NotBlank(message = "El campo description no esta presente")
    private String description;
    private TaskStatusDto status;
    private UserDto user;
}
