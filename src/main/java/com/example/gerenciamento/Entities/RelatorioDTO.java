package com.example.gerenciamento.Entities;

public class RelatorioDTO {

    private String nome;
    private long totalVendido;
    private double precoUnitario;  // Este campo é necessário para calcular o valorTotal
    private double valorTotal;

    // Construtor
    public RelatorioDTO(String nome, long totalVendido, double precoUnitario) {
        this.nome = nome;
        this.totalVendido = totalVendido;
        this.precoUnitario = precoUnitario;
        this.valorTotal = precoUnitario * totalVendido;  // Calcular o valorTotal diretamente
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(long totalVendido) {
        this.totalVendido = totalVendido;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
        this.valorTotal = precoUnitario * totalVendido;  // Recalcular valorTotal sempre que o preço unitário mudar
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
