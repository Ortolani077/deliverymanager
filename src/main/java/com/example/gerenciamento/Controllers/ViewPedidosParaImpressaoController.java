package com.example.gerenciamento.Controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;
import com.example.gerenciamento.Services.ViewPedidosParaImpressaoService;

@RestController
@RequestMapping("/api/pedidos-impressao")
@CrossOrigin(origins = "*") // Ajuste conforme o frontend em produção
public class ViewPedidosParaImpressaoController {

    private final ViewPedidosParaImpressaoService service;

    public ViewPedidosParaImpressaoController(ViewPedidosParaImpressaoService service) {
        this.service = service;
    }

    /**
     * Busca os pedidos para impressão dentro de um intervalo de datas.
     * Exemplo de chamada:
     * /api/pedidos-impressao/data?inicio=2024-08-01T00:00:00&fim=2024-08-15T23:59:59
     */
    @GetMapping("/data")
    public List<ViewPedidosParaImpressao> buscarPorDatas(
        @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return service.buscarPorIntervaloDeDatas(inicio, fim);
    }

    /**
     * Retorna todos os pedidos para impressão.
     */
    @GetMapping("/todos")
    public List<ViewPedidosParaImpressao> buscarTodos() {
        return service.buscarTodos();
    }

    /**
     * Busca os pedidos para impressão com base em um nome de cliente.
     * Exemplo de chamada:
     * /api/pedidos-impressao/cliente?nome=João
     */
    
}
