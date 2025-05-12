package com.example.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gerenciamento.Entities.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
