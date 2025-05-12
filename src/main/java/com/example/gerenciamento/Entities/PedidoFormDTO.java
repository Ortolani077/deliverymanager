package com.example.gerenciamento.Entities;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PedidoFormDTO {

    @NotBlank(message = "Nome do cliente não pode ser vazio")
    @Size(min = 2, max = 100, message = "Nome do cliente deve ter entre 2 e 100 caracteres")
    private String nomeCliente;

    @NotBlank(message = "Email do cliente não pode ser vazio")
    @Email(message = "Email inválido")
    private String emailCliente;

    @NotBlank(message = "Telefone do cliente não pode ser vazio")
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    private String telefoneCliente;

    @NotBlank(message = "Endereço do cliente não pode ser vazio")
    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
    private String enderecoCliente;

    @NotNull(message = "O campo 'entrega' deve ser informado")
    private Boolean entrega;

    @Size(max = 500, message = "Observações não podem ter mais de 500 caracteres")
    private String observacoes;

    @NotNull(message = "O preço não pode ser nulo")
    @Min(value = 0, message = "O preço deve ser maior ou igual a 0")
    private Double preco;

    @NotNull(message = "A lista de itens não pode ser nula")
    @Size(min = 1, message = "Deve haver pelo menos um item no pedido")
    private List<ItemPedidoFormDTO> itens;

    // Getters e setters
    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public Boolean getEntrega() {
        return entrega;
    }

    public void setEntrega(Boolean entrega) {
        this.entrega = entrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<ItemPedidoFormDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoFormDTO> itens) {
        this.itens = itens;
    }

    public static class ItemPedidoFormDTO {
        @NotNull(message = "ID do produto não pode ser nulo")
        private Long produtoId;

        @NotNull(message = "Lista de IDs de produto não pode ser nula")
        private List<Long> produtoIds;

        @Min(value = 1, message = "Quantidade deve ser maior ou igual a 1")
        private int quantidade;

        @Min(value = 0, message = "Preço deve ser maior ou igual a 0")
        private double preco;

        // Getters e setters
        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public List<Long> getProdutoIds() {
            return produtoIds;
        }

        public void setProdutoIds(List<Long> produtoIds) {
            this.produtoIds = produtoIds;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public double getPreco() {
            return preco;
        }

        public void setPreco(double preco) {
            this.preco = preco;
        }
    }
    
    public boolean isEntrega() {
        return entrega != null && entrega; // Returns true if entrega is true, false otherwise.
    }

}
