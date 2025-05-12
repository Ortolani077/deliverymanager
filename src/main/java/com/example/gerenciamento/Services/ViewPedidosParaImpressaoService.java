package com.example.gerenciamento.Services;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;
import com.example.gerenciamento.repositories.ViewPedidosParaImpressaoRepository;

@Service
public class ViewPedidosParaImpressaoService {

    private final ViewPedidosParaImpressaoRepository repository;

    public ViewPedidosParaImpressaoService(ViewPedidosParaImpressaoRepository repository) {
        this.repository = repository;
    }

    public List<ViewPedidosParaImpressao> buscarPorIntervaloDeDatas(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByDataPedidoBetween(inicio, fim);
    }

    public List<ViewPedidosParaImpressao> buscarTodos() {
        return repository.findAll();
    }
}
