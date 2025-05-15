package com.example.gerenciamento.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;  // Importar para usar @JsonIgnoreProperties

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"categorias"})  // Ignora o campo "categorias" na serialização do Cardapio
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    private String descricao;

    @OneToMany(mappedBy = "cardapio")
    private List<Categoria> categorias;
    
    
    
    
    // Construtor usado nos testes
    public Cardapio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
    
    

}
