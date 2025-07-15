package com.nuevospa.tasks.model;

import lombok.*;

@Getter
@Setter
@Builder
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private TaskStatusDto status;
    private UserDto user;
}
