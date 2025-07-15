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
    @NotBlank(message = "El campo username no esta presente")
    private String username;
    @NotBlank(message = "El campo password no esta presente")
    private String password;
    private String role;
}