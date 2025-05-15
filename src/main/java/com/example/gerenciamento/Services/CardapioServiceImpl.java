package com.example.gerenciamento.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.Cardapio;
import com.example.gerenciamento.repositories.CardapioRepository;

@Service
public class CardapioServiceImpl implements CardapioService {

    @Autowired
    private CardapioRepository cardapioRepository;

    @Override
    public List<Cardapio> getAllCardapios() {
        return cardapioRepository.findAll();
    }

    @Override
    public Optional<Cardapio> getCardapioById(Long id) {
        return cardapioRepository.findById(id);
    }

    @Override
    public Cardapio saveCardapio(Cardapio cardapio) {
        return cardapioRepository.save(cardapio);
    }

    @Override
    public Cardapio updateCardapio(Long id, Cardapio cardapio) {
        return cardapioRepository.findById(id).map(existing -> {
            existing.setNome(cardapio.getNome());
            // Adicione aqui qualquer outro campo que precise ser atualizado
            return cardapioRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Cardápio não encontrado com id: " + id));
    }

    @Override
    public void deleteCardapio(Long id) {
        if (!cardapioRepository.existsById(id)) {
            throw new RuntimeException("Cardápio não encontrado com id: " + id);
        }
        cardapioRepository.deleteById(id);
    }
}
