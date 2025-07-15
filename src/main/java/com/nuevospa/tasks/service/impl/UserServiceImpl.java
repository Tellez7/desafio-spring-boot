package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.exception.DataIntegrityViolationException;
import com.nuevospa.tasks.exception.ResourceNotFoundException;
import com.nuevospa.tasks.model.UserDto;
import com.nuevospa.tasks.repository.UserRepository;
import com.nuevospa.tasks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;         // BCrypt

    @Override
    public List<UserDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario " + id + " no encontrado"));
    }

    @Override
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Username " + username + " no encontrado"));
    }

    @Override
    public UserDto create(UserDto dto, String username) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DataIntegrityViolationException("Username repetido, escoja uno diferente");
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
        return entityToDto(userRepository.save(entity));
    }

    @Override
    public UserDto update(Long id, UserDto dto, String username) {
        UserDto userFound = findById(id);
        userFound.setUsername(dto.getUsername());
        userFound.setPassword(encoder.encode(dto.getPassword()));
        userFound.setRole(dto.getRole());
        return entityToDto(userRepository.save(dtoToEntity(userFound)));
    }

    @Override
    public UserDto patch(Long id, Map<String, Object> changes, String username) {
        UserDto userFound = findById(id);
        //TODO: cambiar por generico
        if (changes.containsKey("username")) {
            userFound.setUsername((String) changes.get("username"));
        }

        if (changes.containsKey("password")) {
            userFound.setPassword((String) changes.get("password"));
        }

        if (changes.containsKey("role")) {
            userFound.setRole((String) changes.get("role"));
        }

        return entityToDto(userRepository.save(dtoToEntity(userFound)));
    }

    @Override
    public void delete(Long id, String username) {
        UserDto userFound = findById(id);
        userRepository.delete(dtoToEntity(userFound));
    }

    //TODO: mapper
    @Override
    public UserDto entityToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserEntity dtoToEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }
}
