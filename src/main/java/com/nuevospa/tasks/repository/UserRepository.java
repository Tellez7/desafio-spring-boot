package com.nuevospa.tasks.repository;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.util.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findByRole(Role role, Pageable pageable);

    boolean existsByUsername(String username);
}
