package com.example.gerenciamento.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;

public interface ViewPedidosParaImpressaoRepository extends JpaRepository<ViewPedidosParaImpressao, Long> {

    // Alterando para aceitar LocalDateTime como parâmetros
    List<ViewPedidosParaImpressao> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
}
