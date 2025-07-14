package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.TaskEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos para la entidad TaskEntity.
 * Hereda las operaciones CRUD básicas de JpaRepository.
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Lista todas las tareas pertenecientes a un usuario, ordenadas por id asc.
     * <p>
     * Se usa EntityGraph para traer de una vez el estado y evitar N+1.
     */
    //TODO: check attributePaths, check metodo
    @EntityGraph(attributePaths = {"status"})
    List<TaskEntity> findByOwnerUsernameOrderByIdAsc(String username);

    /**
     * Trae una sola tarea por id y dueño (útil para validar autorización).
     */
    //TODO: check attributePaths
    @EntityGraph(attributePaths = {"status"})
    Optional<TaskEntity> findByIdAndOwnerUsername(Long id, String username);
}
