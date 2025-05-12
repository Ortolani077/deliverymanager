package com.example.gerenciamento.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable // garante que o Hibernate não tente fazer updates/deletes
@Table(name = "pedidos_para_impressao")
public class ViewPedidosParaImpressao {


    @Id
    @Column(name = "pedido_id") 
    private Long pedidoId;
    
    private String nomeCliente;
    @Column(name = "cliente_endereco")
    private String enderecoCliente;
    private Double preco;
    private String observacoes;
    private LocalDateTime dataPedido;
    @Column(name = "tipo_entrega")
    private String entrega;

    public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public String isEntrega() {
		return entrega;
	}

	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}


	// Getters e setters
    public Long getId() {
        return pedidoId;
    }

    public void setId(Long id) {
        this.pedidoId = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

   

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
}
