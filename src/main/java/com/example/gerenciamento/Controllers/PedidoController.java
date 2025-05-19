package com.example.gerenciamento.Controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.gerenciamento.Entities.ItemPedido;
import com.example.gerenciamento.Entities.Pedido;
import com.example.gerenciamento.Entities.PedidoFormDTO;
import com.example.gerenciamento.Entities.ViewPedidosParaImpressao;
import com.example.gerenciamento.Services.PedidoService;

@CrossOrigin(origins = "https://deliverymanager.onrender.com")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/listar")
    public ResponseEntity<List<Pedido>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodosPedidos());
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<Pedido> listarPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado");
        }
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/criar")
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoFormDTO pedidoFormDTO) {
        return ResponseEntity.ok(pedidoService.criarPedido(pedidoFormDTO));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Pedido> editarPedido(@PathVariable Long id, @RequestBody PedidoFormDTO pedidoFormDTO) {
        Pedido pedidoAtualizado = pedidoService.editarPedido(id, pedidoFormDTO);
        return pedidoAtualizado != null ? ResponseEntity.ok(pedidoAtualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirPedido(@PathVariable Long id) {
        return pedidoService.excluirPedido(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/listar-view")
    public ResponseEntity<List<ViewPedidosParaImpressao>> listarPedidosDaView() {
        return ResponseEntity.ok(pedidoService.listarPedidosDaView());
    }

    @GetMapping("/listar-por-data")
    public ResponseEntity<List<Pedido>> listarPedidosPorData(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inicio = LocalDate.parse(dataInicio, formatter);
            LocalDate fim = LocalDate.parse(dataFim, formatter);
            return ResponseEntity.ok(pedidoService.listarPedidosPorData(inicio, fim));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    
    // Listar todos os pedidos que ainda não foram impressos
    @GetMapping("/para-impressao")
    public ResponseEntity<?> listarPedidosParaImpressao() {
        List<Pedido> pedidos = pedidoService.listarPedidosNaoImpressos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    // Marcar pedido como impresso (sem imprimir fisicamente)
    @PutMapping("/{id}/marcar-impresso")
    public ResponseEntity<?> marcarPedidoComoImpresso(@PathVariable Long id) {
        boolean atualizado = pedidoService.marcarComoImpresso(id);
        return atualizado
            ? ResponseEntity.ok("Pedido marcado como impresso.")
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
    }

    // Imprimir pedido fisicamente e marcar como impresso
    @PutMapping("/{id}/imprimir")
    public ResponseEntity<?> imprimirPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        try {
            // Formatar o texto para a impressora
            String texto = formatarParaImpressora(pedido);

            // Enviar para a impressora térmica
            enviarParaImpressoraTermica(texto);

            // Atualizar o status do pedido para "impresso"
            pedido.setImpresso(true);
            pedidoService.atualizarPedido(pedido);

            return ResponseEntity.ok("Pedido impresso com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao imprimir: " + e.getMessage());
        }
    }

    // Geração do texto formatado para a impressora
   
    
    
    
    
    public String gerarTextoParaImpressao(Pedido pedido) {
        StringBuilder texto = new StringBuilder();

        texto.append("==================== PEDIDO Nº ").append(pedido.getId()).append("\n");
        texto.append("====================\n\n");
        
        texto.append("Cliente : ").append(pedido.getCliente().getNome()).append("\n");
        texto.append("Telefone: ").append(pedido.getCliente().getTelefone()).append("\n");
        texto.append("Data    : ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getDataPedido())).append("\n");
        texto.append("Entrega : ").append(pedido.isEntrega() ? "SIM" : "NÃO").append("\n");

        if (pedido.isEntrega()) {
            texto.append("Endereço: ").append(pedido.getEnderecoCliente()).append("\n");
        }

        texto.append("\nItens:\n");

        for (ItemPedido item : pedido.getItens()) {
            texto.append(" - ").append(item.getProduto().getNome())
                 .append("  x").append(item.getQuantidade())
                 .append("  R$ ").append(String.format("%.2f", item.getPreco())).append("\n");
        }

        texto.append("\nTOTAL   : R$ ").append(String.format("%.2f", pedido.getPreco())).append("\n");

        if (pedido.getObservacoes() != null && !pedido.getObservacoes().isEmpty()) {
            texto.append("Obs     : ").append(pedido.getObservacoes()).append("\n");
        }

        texto.append("\n------------------------------\n");

        return texto.toString();
    }

    

    private void enviarParaImpressoraTermica(String texto) throws Exception {
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            throw new Exception("Nenhuma impressora térmica padrão encontrada.");
        }

        byte[] bytes = texto.replaceAll("[^\\x00-\\x7F]", "").getBytes("CP437");

        DocPrintJob job = printService.createPrintJob();
        Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        job.print(doc, null);
    }
    
    @GetMapping("/{id}/dados-para-impressao")
    public ResponseEntity<?> obterTextoParaImpressao(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        String texto = formatarParaImpressora(pedido);
        return ResponseEntity.ok(texto);
    }
    
  

}
