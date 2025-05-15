package com.example.gerenciamento.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.gerenciamento.Entities.Role;
import com.example.gerenciamento.Entities.User;
import com.example.gerenciamento.repositories.UserRepository;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_Sucesso() {
        // Preparando dados
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        User user = new User();
        user.setUsername("usuarioTeste");
        user.setPassword("senha123");
        user.setRoles(Set.of(role));

        // Mock do repository
        when(userRepository.findByUsername("usuarioTeste")).thenReturn(Optional.of(user));

        // Executa
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("usuarioTeste");

        // Verificações
        assertNotNull(userDetails);
        assertEquals("usuarioTeste", userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());

        // Verifica se as authorities contém a role
        assertTrue(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_ADMIN")));

        verify(userRepository).findByUsername("usuarioTeste");
    }

    @Test
    void testLoadUserByUsername_UsuarioNaoEncontrado() {
        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("inexistente");
        });

        assertEquals("Usuário não encontrado: inexistente", exception.getMessage());
        verify(userRepository).findByUsername("inexistente");
    }
}
