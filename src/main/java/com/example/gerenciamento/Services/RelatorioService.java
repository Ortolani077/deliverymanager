package com.example.gerenciamento.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.Produto;
import com.example.gerenciamento.repositories.ItemPedidoRepository;
import com.example.gerenciamento.repositories.ProdutoRepository;

@Service
public class RelatorioService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;

    public RelatorioService(ItemPedidoRepository itemPedidoRepository, ProdutoRepository produtoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Map<String, Object>> getProdutosMaisVendidos() {
        List<Object[]> resultados = itemPedidoRepository.findMostSoldProducts();
        List<Map<String, Object>> relatorio = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Long produtoId = (Long) resultado[0];
            Long quantidadeVendida = ((Number) resultado[1]).longValue(); // Correção para conversão segura

            // Buscar nome do produto pelo ID
            Produto produto = produtoRepository.findById(produtoId).orElse(null);
            String nomeProduto = (produto != null) ? produto.getNome() : "Produto não encontrado";

            // Adiciona ao relatório
            Map<String, Object> item = new HashMap<>();
            item.put("produtoId", produtoId);
            item.put("nomeProduto", nomeProduto);
            item.put("quantidadeVendida", quantidadeVendida);

            relatorio.add(item);
        }

        return relatorio;
    }
}
