package com.example.gerenciamento.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciamento.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Método para encontrar um usuário pelo nome de usuário e retornar um Optional<User>
    Optional<User> findByUsername(String username);
	
    // Método para verificar se um usuário com o nome de usuário já existe
    boolean existsByUsername(String username);
}
