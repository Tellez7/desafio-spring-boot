package com.nuevospa.tasks.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
