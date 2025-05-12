package com.example.gerenciamento.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciamento.Entities.Endereco;
import com.example.gerenciamento.Entities.ItemPedido;
import com.example.gerenciamento.Entities.ItemPedidoDTO;
import com.example.gerenciamento.Entities.Pedido;
import com.example.gerenciamento.Entities.PedidoDTO;
import com.example.gerenciamento.Entities.PedidoFormDTO;
import com.example.gerenciamento.Entities.Produto;
import com.example.gerenciamento.Entities.ProdutoDTO;
import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;
import com.example.gerenciamento.repositories.PedidoRepository;
import com.example.gerenciamento.repositories.ProdutoRepository;
import com.example.gerenciamento.repositories.ViewPedidosParaImpressaoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ViewPedidosParaImpressaoRepository pedidosParaImpressaoRepository;

    // Listar todos os pedidos da view
    public List<ViewPedidosParaImpressao> listarPedidosDaView() {
        return pedidosParaImpressaoRepository.findAll();
    }

    // Listar pedidos por intervalo de datas
    public List<Pedido> listarPedidosPorData(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null || dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("As datas fornecidas são inválidas.");
        }
        return pedidoRepository.findPedidosByDateRange(dataInicio, dataFim);
    }

    // Listar todos os pedidos
    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por ID
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido com ID " + id + " não encontrado."));
    }

    // Criar novo pedido
    public Pedido criarPedido(PedidoFormDTO pedidoFormDTO) {
        Pedido novoPedido = new Pedido();
        novoPedido.setNomeCliente(pedidoFormDTO.getNomeCliente());
        novoPedido.setTelefoneCliente(pedidoFormDTO.getTelefoneCliente());
        novoPedido.setEnderecoCliente(pedidoFormDTO.getEnderecoCliente());
        novoPedido.setEntrega(pedidoFormDTO.getEntrega());
        novoPedido.setObservacoes(pedidoFormDTO.getObservacoes());
        novoPedido.setPreco(pedidoFormDTO.getPreco());
        return pedidoRepository.save(novoPedido);
    }

    // Editar pedido existente
    public Pedido editarPedido(Long id, PedidoFormDTO pedidoFormDTO) {
        Pedido pedidoExistente = buscarPedidoPorId(id); // Reutilizando o método buscarPedidoPorId
        pedidoExistente.setNomeCliente(pedidoFormDTO.getNomeCliente());
        pedidoExistente.setTelefoneCliente(pedidoFormDTO.getTelefoneCliente());
        pedidoExistente.setEnderecoCliente(pedidoFormDTO.getEnderecoCliente());
        pedidoExistente.setEntrega(pedidoFormDTO.getEntrega());
        pedidoExistente.setObservacoes(pedidoFormDTO.getObservacoes());
        pedidoExistente.setPreco(pedidoFormDTO.getPreco());
        return pedidoRepository.save(pedidoExistente);
    }

    // Excluir pedido
    public boolean excluirPedido(Long id) {
        Pedido pedidoExistente = buscarPedidoPorId(id); // Reutilizando o método buscarPedidoPorId
        pedidoRepository.delete(pedidoExistente);
        return true;
    }

    // Adicionar produto ao pedido
    public void adicionarProdutoAoPedido(Long pedidoId, Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        Pedido pedido = buscarPedidoPorId(pedidoId);
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto com ID " + produtoId + " não encontrado."));

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setNomeProduto(produto.getNome());
        itemPedido.setQuantidade(quantidade);
        itemPedido.setPreco(produto.getPreco());

        pedido.getItens().add(itemPedido);
        pedidoRepository.save(pedido);
    }
    
    public PedidoDTO converterParaDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        
        // Mapeando dados básicos
        dto.setClienteId(pedido.getCliente().getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setObservacoes(pedido.getObservacoes());
        dto.setPreco(pedido.getPreco());
        dto.setNomeCliente(pedido.getCliente().getNome());
        dto.setTelefoneCliente(pedido.getCliente().getTelefone());

        // Adicionando a lista de endereços ao DTO
        List<Endereco> enderecosCliente = pedido.getCliente().getEnderecos();
        dto.setEnderecoCliente(enderecosCliente); // Agora é uma lista de Endereco

        // Mapeando os itens do pedido
        List<ItemPedidoDTO> itemDTOs = new ArrayList<>();
        for (ItemPedido item : pedido.getItens()) {
            ItemPedidoDTO itemDTO = new ItemPedidoDTO();
            itemDTO.setId(item.getId());
            itemDTO.setQuantidade(item.getQuantidade());
            
            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setId(item.getProduto().getId());
            produtoDTO.setNome(item.getProduto().getNome());
            produtoDTO.setDescricao(item.getProduto().getDescricao());
            produtoDTO.setPreco(item.getProduto().getPreco());

            itemDTO.setProduto(produtoDTO);
            itemDTOs.add(itemDTO);
        }

        dto.setItens(itemDTOs);
        dto.setImpresso(pedido.isImpresso());

        return dto;
    }


    
    public List<PedidoDTO> buscarPedidosDTOComItens(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoBetween(inicio, fim);
        return pedidos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }



    // Remover produto do pedido
    public void removerProdutoDoPedido(Long pedidoId, Long produtoId) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        boolean removido = pedido.getItens().removeIf(item -> item.getProduto().getId().equals(produtoId));
        if (!removido) {
            throw new RuntimeException("Produto com ID " + produtoId + " não encontrado no pedido.");
        }
        pedidoRepository.save(pedido);
    }

    // Listar todos os produtos de um pedido
    public List<ItemPedido> listarProdutosDoPedido(Long pedidoId) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        return pedido.getItens();
    }

    // Atualizar a quantidade de um produto no pedido
    public void atualizarQuantidadeProduto(Long pedidoId, Long produtoId, int novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        Pedido pedido = buscarPedidoPorId(pedidoId);

        ItemPedido itemPedido = pedido.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produto com ID " + produtoId + " não encontrado no pedido."));

        itemPedido.setQuantidade(novaQuantidade);
        pedidoRepository.save(pedido);
    }
    
    
    public List<Pedido> listarPedidosNaoImpressos() {
        return pedidoRepository.findByImpressoFalse();
    }

    public boolean marcarComoImpresso(Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setImpresso(true);
            pedidoRepository.save(pedido);
            return true;
        }
        return false;
    }
    
    
    public Pedido atualizarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }


}
