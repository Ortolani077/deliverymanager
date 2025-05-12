package com.example.gerenciamento.Services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.RelatorioDTO;
import com.example.gerenciamento.repositories.RelatorioRepository;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    public List<RelatorioDTO> getProdutosMaisVendidos() {
        List<RelatorioDTO> resultados = relatorioRepository.buscarMaisVendidos();

        return resultados.stream()
                .map(item -> {
                    double valorTotal = item.getPrecoUnitario() * item.getTotalVendido();
                    item.setValorTotal(valorTotal);
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<RelatorioDTO> getProdutosMaisVendidosPorPeriodo(LocalDate inicio, LocalDate fim) {
        // Converte LocalDate para java.util.Date
        Date dataInicio = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dataFim = Date.from(fim.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        List<RelatorioDTO> resultados = relatorioRepository.buscarItensPorPeriodo(dataInicio, dataFim);

        return resultados.stream()
                .map(item -> {
                    double valorTotal = item.getPrecoUnitario() * item.getTotalVendido();
                    item.setValorTotal(valorTotal);
                    return item;
                })
                .collect(Collectors.toList());
    }
}
