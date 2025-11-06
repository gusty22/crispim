package com.finan.orcamento.controller;

import com.finan.orcamento.model.ClienteModel;
import com.finan.orcamento.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // 1. Endpoint para servir a PÁGINA HTML
    @GetMapping("/clientes")
    public String paginaClientes() {
        return "clientePage";
    }

    // --- API REST PARA CLIENTES ---

    // 2. API para LISTAR
    @GetMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> listarTodosClientes() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    // 3. API para OBTER POR ID
    @GetMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<ClienteModel> obterClientePorId(@PathVariable Long id) {
        try {
            ClienteModel cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. API para SALVAR (Criar/Atualizar)
    @PostMapping("/api/clientes")
    @ResponseBody
    public ResponseEntity<ClienteModel> salvarOuAtualizarCliente(@RequestBody ClienteModel clienteModel) {
        ClienteModel clienteSalvo = clienteService.salvarOuAtualizar(clienteModel);
        return ResponseEntity.ok(clienteSalvo);
    }

    // 5. API para DELETAR
    @DeleteMapping("/api/clientes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. API DE PESQUISA (para a tela de orçamento)
    @GetMapping("/api/clientes/pesquisa")
    @ResponseBody
    public ResponseEntity<List<ClienteModel>> pesquisarClientes(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(clienteService.pesquisarPorNomeOuCpf(termo));
    }
}