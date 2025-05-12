package com.example.gerenciamento.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gerenciamento.Entities.RelatorioDTO;
import com.example.gerenciamento.Entities.ItemPedido;

@Repository
public interface RelatorioRepository extends JpaRepository<ItemPedido, Long> {

    // Consulta para buscar os produtos mais vendidos, com preço unitário
    @Query("SELECT new com.example.gerenciamento.Entities.RelatorioDTO(i.produto.nome, COUNT(i), i.produto.preco) " +
           "FROM ItemPedido i " +
           "GROUP BY i.produto.nome, i.produto.preco " +
           "ORDER BY COUNT(i) DESC")
    List<RelatorioDTO> buscarMaisVendidos();

    // Consulta para buscar os itens de pedidos entre um período específico
    @Query("SELECT new com.example.gerenciamento.Entities.RelatorioDTO(i.produto.nome, COUNT(i), i.produto.preco) " +
           "FROM ItemPedido i " +
           "WHERE i.pedido.dataPedido BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY i.produto.nome, i.produto.preco " +
           "ORDER BY COUNT(i) DESC")
    List<RelatorioDTO> buscarItensPorPeriodo(@Param("dataInicio") Date dataInicio,
                                             @Param("dataFim") Date dataFim);
}
