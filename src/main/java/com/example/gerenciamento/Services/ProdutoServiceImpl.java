package com.example.gerenciamento.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.Produto;
import com.example.gerenciamento.repositories.ProdutoRepository;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public Optional<Produto> getProdutoById(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public Produto createProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Produto updateProduto(Long id, Produto produto) {
        if (produtoRepository.existsById(id)) {
            produto.setId(id);
            return produtoRepository.save(produto);
        }
        return null;
    }

    @Override
    public void deleteProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public List<Produto> listAll() {
        return produtoRepository.findAll();
    }

    @Override
    public List<Produto> getProdutosByCategoria(Long categoriaId) {
        return produtoRepository.findProdutosByCategoria(categoriaId);
    }

    @Override
    public List<Object[]> getProdutosByCategoriaNative(Long categoriaId) {
        return produtoRepository.findProdutosByCategoriaNative(categoriaId);
    }
}
