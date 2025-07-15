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
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private TaskStatusDto status;
    private UserDto user;
}
