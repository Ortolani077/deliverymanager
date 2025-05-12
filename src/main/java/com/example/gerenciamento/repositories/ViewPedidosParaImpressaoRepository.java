package com.example.gerenciamento.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;

public interface ViewPedidosParaImpressaoRepository extends JpaRepository<ViewPedidosParaImpressao, Long> {

    // Método para buscar pedidos entre as datas fornecidas
    List<ViewPedidosParaImpressao> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Se precisar de uma consulta personalizada, use a anotação @Query
    @Query("SELECT v FROM ViewPedidosParaImpressao v")
    List<ViewPedidosParaImpressao> findAllPedidos();
}
