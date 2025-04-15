package com.example.gerenciamento.Services;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gerenciamento.DTOs.AuthResponse;
import com.example.gerenciamento.DTOs.UserRegistrationRequest;
import com.example.gerenciamento.Entities.Role;
import com.example.gerenciamento.Entities.User;
import com.example.gerenciamento.repositories.RoleRepository;
import com.example.gerenciamento.repositories.UserRepository;
import com.example.gerenciamento.Security.JwtTokenProvider;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse registerUser(UserRegistrationRequest request) {
        System.out.println("Registrando usuário: " + request.getUsername());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());

        Role role = roleRepository.findByName(request.getRole());
        if (role == null) {
            role = new Role();
            role.setName(request.getRole());
            roleRepository.save(role);
        }

        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        // Coletar os nomes das roles em uma lista de strings
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.generateToken(user.getUsername(), roles);

        return new AuthResponse(token);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
