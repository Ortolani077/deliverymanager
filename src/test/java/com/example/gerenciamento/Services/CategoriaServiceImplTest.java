package com.example.gerenciamento.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.gerenciamento.Entities.Cardapio;
import com.example.gerenciamento.Entities.Categoria;
import com.example.gerenciamento.repositories.CategoriaRepository;

class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Cardapio cardapio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardapio = new Cardapio(null, null);
        cardapio.setId(1L);
    }

    @Test
    void testGetAllCategorias() {
        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNome("Bebidas");
        cat1.setCardapio(cardapio);

        Categoria cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNome("Lanches");
        cat2.setCardapio(cardapio);

        List<Categoria> categorias = new ArrayList<>();
        categorias.add(cat1);
        categorias.add(cat2);

        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> result = categoriaService.getAllCategorias();

        assertEquals(2, result.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoriaById() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Sobremesas");
        categoria.setCardapio(cardapio);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> result = categoriaService.getCategoriaById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sobremesas", result.get().getNome());
        assertEquals(1L, result.get().getCardapio().getId());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void testSaveCategoria() {
        Categoria nova = new Categoria();
        nova.setNome("Pizzas");
        nova.setCardapio(cardapio);

        Categoria salva = new Categoria();
        salva.setId(3L);
        salva.setNome("Pizzas");
        salva.setCardapio(cardapio);

        when(categoriaRepository.save(nova)).thenReturn(salva);

        Categoria result = categoriaService.saveCategoria(nova);

        assertNotNull(result.getId());
        assertEquals("Pizzas", result.getNome());
        assertEquals(1L, result.getCardapio().getId());
        verify(categoriaRepository).save(nova);
    }

    @Test
    void testUpdateCategoriaComSucesso() {
        Categoria existente = new Categoria();
        existente.setId(1L);
        existente.setNome("Antigo Nome");
        existente.setCardapio(cardapio);

        Categoria nova = new Categoria();
        nova.setNome("Novo Nome");
        nova.setCardapio(cardapio);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existente);

        Categoria atualizada = categoriaService.updateCategoria(1L, nova);

        assertEquals("Novo Nome", atualizada.getNome());
        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).save(existente);
    }

    @Test
    void testUpdateCategoriaNaoEncontrada() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.updateCategoria(99L, new Categoria());
        });

        assertEquals("Categoria não encontrada com id: 99", exception.getMessage());
        verify(categoriaRepository).findById(99L);
    }

    @Test
    void testDeleteCategoriaComSucesso() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.deleteCategoria(1L);

        verify(categoriaRepository).existsById(1L);
        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void testDeleteCategoriaNaoEncontrada() {
        when(categoriaRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.deleteCategoria(999L);
        });

        assertEquals("Categoria não encontrada com id: 999", exception.getMessage());
        verify(categoriaRepository).existsById(999L);
        verify(categoriaRepository, never()).deleteById(anyLong());
    }
}
