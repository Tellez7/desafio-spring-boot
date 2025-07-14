package com.nuevospa.tasks.entity;

//TDO: check
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;   // hash BCrypt
    private String role;       // p. ej. "ROLE_USER"
}