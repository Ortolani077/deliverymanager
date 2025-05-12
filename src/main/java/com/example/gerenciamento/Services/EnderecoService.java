package com.example.gerenciamento.Services;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.Endereco;
import com.example.gerenciamento.repositories.EnderecoRepository;


@Service
public class EnderecoService {
   
	
	
	private final EnderecoRepository enderecoRepository; 
	

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }
	
 public List<Endereco> getAllEndereco() {
    return (List<Endereco>) enderecoRepository.findAll();
}

public Optional<Endereco> getClienteById(Long id) {
    return enderecoRepository.findById(id);
}

public Endereco createEndereco(Endereco endereco) {
    return enderecoRepository.save(endereco);
}






public Endereco updateEndereco(Long id, Endereco endereco) {
    if (enderecoRepository.existsById(id)) {
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }
    return null; // Ou lança uma exceção indicando que o cliente não foi encontrado
}

public boolean deleteEndereco(Long id) {
    if (enderecoRepository.existsById(id)) {
    	enderecoRepository.deleteById(id);
        return true;
    }
    return false; // Ou lança uma exceção indicando que o cliente não foi encontrado
}
}