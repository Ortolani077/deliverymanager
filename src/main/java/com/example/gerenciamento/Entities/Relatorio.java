package com.example.gerenciamento.Entities;

import java.util.List;

public class Relatorio {

    private List<RelatorioDTO> relatorios;

    // Construtor
    public Relatorio(List<RelatorioDTO> relatorios) {
        this.relatorios = relatorios;
    }

    // Método para adicionar um item ao relatório
    public void adicionarRelatorio(RelatorioDTO relatorioDTO) {
        this.relatorios.add(relatorioDTO);
    }

    // Método para calcular o total geral do valor total de vendas
    public double calcularTotalGeral() {
        double totalGeral = 0;
        for (RelatorioDTO relatorio : relatorios) {
            totalGeral += relatorio.getValorTotal();
        }
        return totalGeral;
    }


    // Getter e Setter
    public List<RelatorioDTO> getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(List<RelatorioDTO> relatorios) {
        this.relatorios = relatorios;
    }
}
