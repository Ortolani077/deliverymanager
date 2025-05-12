package com.example.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gerenciamento.Entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
