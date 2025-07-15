package com.nuevospa.tasks.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String role;
}