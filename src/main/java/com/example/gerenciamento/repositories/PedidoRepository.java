package com.example.gerenciamento.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gerenciamento.Entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) BETWEEN :dataInicio AND :dataFim")
    List<Pedido> findPedidosByDateRange(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    List<Pedido> findByImpressoFalse();
    
    
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

}
