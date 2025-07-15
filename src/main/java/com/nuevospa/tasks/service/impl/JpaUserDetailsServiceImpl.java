package com.nuevospa.tasks.service.impl;

import com.nuevospa.tasks.entity.UserEntity;
import com.nuevospa.tasks.exception.UsernameNotFoundException;
import com.nuevospa.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class JpaUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();
    }
}
