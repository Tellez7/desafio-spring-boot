package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos para la entidad User.
 * Spring Data implementará automáticamente los métodos declarados.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario (único)
     * @return Optional con el usuario si existe
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Verifica si existe un usuario con ese nombre.
     *
     * @param username nombre de usuario
     * @return true si ya está registrado
     */
    boolean existsByUsername(String username);
}
