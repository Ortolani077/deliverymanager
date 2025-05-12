package com.example.gerenciamento.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciamento.Entities.Produto;
import com.example.gerenciamento.Services.ProdutoService;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PreAuthorize("hasRole('PRODUTO_SELECT')")
    @GetMapping
    public ResponseEntity<List<Produto>> listAll(@RequestParam(value = "categoria", required = false) Long categoriaId) {
        List<Produto> produtos;
        if (categoriaId != null) {
            produtos = produtoService.getProdutosByCategoria(categoriaId);
        } else {
            produtos = produtoService.listAll();
        }
        return ResponseEntity.ok(produtos);
    }


    @PreAuthorize("hasRole('PRODUTO_CREATE')")
    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoService.createProduto(produto);
    }

    @PreAuthorize("hasRole('PRODUTO_UPDATE')")
    @PutMapping("/{id}")
    public Produto updateProduto(@PathVariable Long id, @RequestBody Produto produto) {
        return produtoService.updateProduto(id, produto);
    }

    @PreAuthorize("hasRole('PRODUTO_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        produtoService.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.getProdutoById(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Consulta utilizando JPQL com INNER JOIN
    @PreAuthorize("hasRole('PRODUTO_SELECT')")
    @GetMapping("/categoria/{categoriaId}/jpql")
    public ResponseEntity<List<Produto>> getProdutosByCategoria(@PathVariable Long categoriaId) {
        List<Produto> produtos = produtoService.getProdutosByCategoria(categoriaId);
        return ResponseEntity.ok(produtos);
    }

    // Consulta utilizando SQL nativo com INNER JOIN
    @PreAuthorize("hasRole('PRODUTO_SELECT')")
    @GetMapping("/categoria/{categoriaId}/native")
    public ResponseEntity<List<Object[]>> getProdutosByCategoriaNative(@PathVariable Long categoriaId) {
        List<Object[]> produtos = produtoService.getProdutosByCategoriaNative(categoriaId);
        return ResponseEntity.ok(produtos);
    }

    // Consulta utilizando JPQL com INNER JOIN para "Pizzas"
    @PreAuthorize("hasRole('PRODUTO_SELECT')")
    @GetMapping("/categoria/pizzas")
    public ResponseEntity<List<Produto>> getPizzas() {
        Long categoriaId = 2L; // Categoria para Pizzas
        List<Produto> produtos = produtoService.getProdutosByCategoria(categoriaId);
        return ResponseEntity.ok(produtos);
    }

    @PreAuthorize("hasRole('PRODUTO_SELECT')")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Produto>> obterProdutosPorCategoria(@PathVariable("categoriaId") Long categoriaId) {
        List<Produto> produtos = produtoService.getProdutosByCategoria(categoriaId);
        return ResponseEntity.ok(produtos);
    }
}
