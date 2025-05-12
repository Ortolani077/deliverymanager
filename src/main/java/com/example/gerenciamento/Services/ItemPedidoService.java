package com.example.gerenciamento.Services;


import java.util.List;
import java.util.Optional;

import com.example.gerenciamento.Entities.ItemPedido;

public interface ItemPedidoService {
    List<ItemPedido> getAllItensPedido();

    Optional<ItemPedido> getItemPedidoById(Long id);

    ItemPedido saveItemPedido(ItemPedido itemPedido);

    ItemPedido updateItemPedido(Long id, ItemPedido itemPedido);

    void deleteItemPedido(Long id);
}
