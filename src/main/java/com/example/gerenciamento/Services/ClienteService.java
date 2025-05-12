package com.example.gerenciamento.Services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.gerenciamento.Entities.Cliente;

public interface ClienteService extends UserDetailsService {

	Cliente create(Cliente user);
    // Outros métodos específicos do serviço Cliente, se necessário

	Cliente findByEmail(String email);

	boolean authenticate(String email, String senha);
}
