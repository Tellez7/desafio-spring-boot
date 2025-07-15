package com.nuevospa.tasks.model;

import com.nuevospa.tasks.util.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {

    private Long id;
    @NotNull
    private TaskStatus name;
}