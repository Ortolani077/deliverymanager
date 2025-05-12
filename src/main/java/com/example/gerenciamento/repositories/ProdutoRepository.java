package com.example.gerenciamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.gerenciamento.Entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Consulta JPQL com INNER JOIN
    @Query("SELECT p FROM Produto p JOIN p.categoria c WHERE c.id = :categoriaId")
    List<Produto> findProdutosByCategoria(@Param("categoriaId") Long categoriaId);

    // Consulta SQL nativo
    @Query(value = "SELECT p.id, p.nome, p.preco, p.descricao, c.nome AS categoria " +
                   "FROM produto p INNER JOIN categoria c ON p.categoria_id = c.id " +
                   "WHERE c.id = :categoriaId", nativeQuery = true)
    List<Object[]> findProdutosByCategoriaNative(@Param("categoriaId") Long categoriaId);
}
