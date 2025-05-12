package com.example.gerenciamento.Services;


import java.util.List;
import java.util.Optional;

import com.example.gerenciamento.Entities.Cardapio;

public interface CardapioService {
    List<Cardapio> getAllCardapios();

    Optional<Cardapio> getCardapioById(Long id);

    Cardapio saveCardapio(Cardapio cardapio);

    Cardapio updateCardapio(Long id, Cardapio cardapio);

    void deleteCardapio(Long id);
}
