package com.example.gerenciamento.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciamento.Entities.RelatorioDTO;
import com.example.gerenciamento.Services.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // Endpoint para listar os produtos mais vendidos
    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<RelatorioDTO>> listarProdutosMaisVendidos() {
        List<RelatorioDTO> relatorio = relatorioService.getProdutosMaisVendidos();
        if (relatorio.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Retorna 204 se a lista estiver vazia
        }
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/produtos-mais-vendidos-periodo")
    public ResponseEntity<List<RelatorioDTO>> listarProdutosMaisVendidosPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {

        try {
            // Verifica se as datas foram corretamente convertidas
            System.out.println("Data de Início: " + inicio);
            System.out.println("Data de Fim: " + fim);

            // Realiza a consulta dos produtos mais vendidos dentro do período
            List<RelatorioDTO> relatorioPeriodo = relatorioService.getProdutosMaisVendidosPorPeriodo(inicio, fim);

            if (relatorioPeriodo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(relatorioPeriodo);
        } catch (Exception e) {
            e.printStackTrace();  // Exibe qualquer erro para depuração
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}