package com.example.gerenciamento.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.gerenciamento.Entities.Cardapio;
import com.example.gerenciamento.repositories.CardapioRepository;

class CardapioServiceImplTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @InjectMocks
    private CardapioServiceImpl cardapioService;

    private Cardapio cardapio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardapio = new Cardapio(1L, "Cardápio Teste");
    }

    @Test
    void testGetAllCardapios() {
        List<Cardapio> cardapios = Arrays.asList(cardapio, new Cardapio(2L, "Outro Cardápio"));
        when(cardapioRepository.findAll()).thenReturn(cardapios);

        List<Cardapio> result = cardapioService.getAllCardapios();

        assertEquals(2, result.size());
        verify(cardapioRepository, times(1)).findAll();
    }

    @Test
    void testGetCardapioById() {
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));

        Optional<Cardapio> result = cardapioService.getCardapioById(1L);

        assertTrue(result.isPresent());
        assertEquals("Cardápio Teste", result.get().getNome());
        verify(cardapioRepository).findById(1L);
    }

    @Test
    void testSaveCardapio() {
        when(cardapioRepository.save(cardapio)).thenReturn(cardapio);

        Cardapio result = cardapioService.saveCardapio(cardapio);

        assertNotNull(result);
        assertEquals("Cardápio Teste", result.getNome());
        verify(cardapioRepository).save(cardapio);
    }

    @Test
    void testUpdateCardapio_Sucesso() {
        Cardapio atualizado = new Cardapio(null, "Atualizado");
        when(cardapioRepository.findById(1L)).thenReturn(Optional.of(cardapio));
        when(cardapioRepository.save(any(Cardapio.class))).thenAnswer(invocation -> {
            Cardapio c = invocation.getArgument(0);
            return c;
        });

        Cardapio result = cardapioService.updateCardapio(1L, atualizado);

        assertEquals("Atualizado", result.getNome());
        verify(cardapioRepository).findById(1L);
        verify(cardapioRepository).save(any(Cardapio.class));
    }

    @Test
    void testUpdateCardapio_NaoEncontrado() {
        when(cardapioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardapioService.updateCardapio(99L, new Cardapio(null, "Teste"));
        });

        assertEquals("Cardápio não encontrado com id: 99", exception.getMessage());
        verify(cardapioRepository).findById(99L);
    }

    @Test
    void testDeleteCardapio_Sucesso() {
        when(cardapioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cardapioRepository).deleteById(1L);

        cardapioService.deleteCardapio(1L);

        verify(cardapioRepository).existsById(1L);
        verify(cardapioRepository).deleteById(1L);
    }

    @Test
    void testDeleteCardapio_NaoEncontrado() {
        when(cardapioRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardapioService.deleteCardapio(99L);
        });

        assertEquals("Cardápio não encontrado com id: 99", exception.getMessage());
        verify(cardapioRepository).existsById(99L);
        verify(cardapioRepository, never()).deleteById(anyLong());
    }
}
