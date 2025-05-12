package com.example.gerenciamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.gerenciamento.Entities.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
	
	
	
	   @Query("SELECT i.produto.id, COUNT(i.produto.id) FROM ItemPedido i GROUP BY i.produto.id ORDER BY COUNT(i.produto.id) DESC")
	    List<Object[]> findMostSoldProducts();
	
	
}
