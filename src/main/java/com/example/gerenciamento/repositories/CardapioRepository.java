package com.example.gerenciamento.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gerenciamento.Entities.Cardapio;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {
}
