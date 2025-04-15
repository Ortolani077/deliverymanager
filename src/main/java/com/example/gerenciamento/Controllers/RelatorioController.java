package com.example.gerenciamento.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciamento.Services.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<Map<String, Object>>> getProdutosMaisVendidos() {
        List<Map<String, Object>> relatorio = relatorioService.getProdutosMaisVendidos();
        return ResponseEntity.ok(relatorio);
    }
}

