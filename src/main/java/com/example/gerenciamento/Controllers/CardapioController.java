package com.example.gerenciamento.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciamento.Entities.Cardapio;
import com.example.gerenciamento.repositories.CardapioRepository;

@RestController
@RequestMapping("/api/cardapios")
public class CardapioController {

    @Autowired
    private CardapioRepository cardapioRepository;

    @GetMapping
    public List<Cardapio> getAllCardapios() {
        return cardapioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> getCardapioById(@PathVariable Long id) {
        Optional<Cardapio> cardapio = cardapioRepository.findById(id);
        return cardapio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cardapio createCardapio(@RequestBody Cardapio cardapio) {
        return cardapioRepository.save(cardapio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cardapio> updateCardapio(@PathVariable Long id, @RequestBody Cardapio cardapioDetails) {
        Optional<Cardapio> cardapio = cardapioRepository.findById(id);
        if (cardapio.isPresent()) {
            Cardapio updatedCardapio = cardapio.get();
            updatedCardapio.setNome(cardapioDetails.getNome());
            updatedCardapio.setDescricao(cardapioDetails.getDescricao());
            updatedCardapio.setCategorias(cardapioDetails.getCategorias());
            cardapioRepository.save(updatedCardapio);
            return ResponseEntity.ok(updatedCardapio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardapio(@PathVariable Long id) {
        Optional<Cardapio> cardapio = cardapioRepository.findById(id);
        if (cardapio.isPresent()) {
            cardapioRepository.delete(cardapio.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
